# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)
#
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6b566c5b4da35d474758324899cb4562"

SRC_URI = "git://anongit.freedesktop.org/beignet;branch=Release_v1.1; \
           file://Driver-fix-the-annoying-Failed-to-release-userptr-error-message.patch; "

DEPENDS = "opencl-headers ocl-icd llvm-clang python-dev libdrm libxext libxfixes zlib mesa libgcc libpthread-stubs"
RDEPENDS_${PN} = "ocl-icd llvm-clang libdrm libxext libxfixes zlib mesa libpthread-stubs"

export WANT_LLVM_RELEASE = "3.6"

PV = "1.1.1"
SRCREV = "6be1a61e21647238f640f89c9b4b99443602b3e0"

S = "${WORKDIR}/git"

inherit cmake pythonnative

EXTRA_OECMAKE = " \
                -DCMAKE_BUILD_TYPE=Release \
		-DCMAKE_SKIP_RPATH=TRUE \
		-DCOMPILER=GCC \
		"

FILES_${PN} += " \
                ${sysconfdir}/OpenCL/vendors/intel-beignet.icd \
                ${libdir} \
                ${libdir}/beignet/ \
                ${libdir}/beignet/* \
               "

do_configure_prepend () {
    # beignet uses its own special C/CXX flags; add the sysroot to them
    sed -i "s:set (CMAKE_C_FLAGS \"\${CMAKE_C_CXX_FLAGS}:set \(CMAKE_C_FLAGS \"\${CMAKE_C_CXX_FLAGS} --sysroot=${STAGING_DIR_HOST}:g" ${S}/CMakeLists.txt
    sed -i "s:set (CMAKE_CXX_FLAGS \"\${CMAKE_C_CXX_FLAGS}:set (CMAKE_CXX_FLAGS \"\${CMAKE_C_CXX_FLAGS} --sysroot=${STAGING_DIR_HOST}:g" ${S}/CMakeLists.txt
}

do_compile_prepend () {
    # FIXME: hack; relying on the target sysroot means we have to build to and from x86_64
    export LD_LIBRARY_PATH="${STAGING_DIR_TARGET}/${libdir}:$LD_LIBRARY_PATH"
}

do_compile_append () {
    # FIXME: Fix LD_LIBRARY_PATH after we changed it...
    j=`echo $LD_LIBRARY_PATH | sed "s:^.*\:::"`
    export LD_LIBRARY_PATH="$j"
}

do_install_append () {
    # Remove the headers; these will be included by another recipe
    rm -rf ${D}${includedir}/CL

    # Create intel-beignet.icd file
    mkdir -p ${D}${sysconfdir}/OpenCL/vendors/
    echo ${libdir}/beignet/libcl.so > ${D}${sysconfdir}/OpenCL/vendors/intel-beignet.icd
}

INSANE_SKIP_${PN} = "debug-files"
