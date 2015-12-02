BRANCH = "release_36"
SRCREV_llvm = "19ade095e8c3ea61f84b71074433309f0c7c7b3b"
SRCREV_clang = "c2e93b903ffee0e691757284c53e5f81615bdc55"

require recipes-core/llvm/llvm.inc

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d; \
                    file://tools/clang/LICENSE.TXT;md5=3954ab76dfb9ce9024cdce4c24268267; \
                   "

SRC_URI = "git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/clang.git;branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
           file://0002-xml2-config-is-disabled.-Use-pkg-config-instead.patch \
          "
PACKAGES += " llvm${PV}-clang "

# The extra build tools installed to bindir_crossscripts without ${PV} suffixes may make this conflict with other llvm versions
RCONFLICTS_${PN} = "llvm3.3"

DEPENDS += " libxml2 "
RDEPENDS_${PN} += " libxml2 "

FILES_llvm${PV}-clang = " \
			${libdir}/llvm${PV}/clang \
			${libdir}/llvm${PV}/clang/${PV} \
			${libdir}/llvm${PV}/clang/${PV}/include \
			${libdir}/llvm${PV}/clang/${PV}/include/* \
		"

ALLOW_EMPTY_${PN}-staticdev = "0"

do_compile_class-native() {
    cd ${LLVM_BUILD_DIR}

    # Keep the "Fix libdir for multilib" from llvm.inc, but remove everything else and build
    sed -i 's:(PROJ_prefix)/lib:(PROJ_prefix)${base_libdir}:g' Makefile.config

    oe_runmake
}

do_install_class-target() {
    cd ${LLVM_BUILD_DIR}

    oe_runmake DESTDIR=${LLVM_INSTALL_DIR} install

    # Move these build tools to their own directory so they can be installed into bindir_crossscripts later
    # Beignet needs these utilities to build. It expects them in the same dir as llvm-config and does not expect the HOST_SYS- prefix.
    install -d ${LLVM_INSTALL_DIR}/progs/
    for i in `ls ${LLVM_INSTALL_DIR}${bindir}/ | grep "${HOST_SYS}-" | grep -v "llvm-config"`;
    do
      j=`echo $i | sed "s:${HOST_SYS}-::"`
      cp ${LLVM_INSTALL_DIR}${bindir}/$i ${LLVM_INSTALL_DIR}/progs/$j
    done

    cp ${LLVM_INSTALL_DIR}${bindir}/${HOST_SYS}-llvm-config-host ${LLVM_INSTALL_DIR}/llvm-config-host

    install -d ${D}${bindir}/${LLVM_DIR}
    cp -r ${LLVM_INSTALL_DIR}${bindir}/* ${D}${bindir}/${LLVM_DIR}/

    install -d ${D}${includedir}/${LLVM_DIR}
    cp -r ${LLVM_INSTALL_DIR}${includedir}/* ${D}${includedir}/${LLVM_DIR}/

    install -d ${D}${libdir}/${LLVM_DIR}
    cp -r ${LLVM_INSTALL_DIR}${libdir}/* ${D}${libdir}/${LLVM_DIR}/
    ln -s ${LLVM_DIR}/libLLVM-${PV}.so ${D}${libdir}/libLLVM-${PV}.so

    install -d ${D}${docdir}/${LLVM_DIR}
    cp -r ${LLVM_INSTALL_DIR}${prefix}/docs/llvm/* ${D}${docdir}/${LLVM_DIR}
}

do_install_class-native() {
    cd ${LLVM_BUILD_DIR}

    oe_runmake DESTDIR=${D} install
}


llvm_sysroot_preprocess_append_class-target() {
    cp -r ${LLVM_INSTALL_DIR}/progs/* ${SYSROOT_DESTDIR}${bindir_crossscripts}/
}

llvm_sysroot_preprocess_class-native() {
   exit 0 
}

BBCLASSEXTEND = "native"

INSANE_SKIP_libllvm3.6-llvm-3.6.2 += "dev-so"
