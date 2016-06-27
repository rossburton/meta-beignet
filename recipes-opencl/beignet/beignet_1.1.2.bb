LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6b566c5b4da35d474758324899cb4562"

SRC_URI = "git://anongit.freedesktop.org/beignet \
           file://more-broxton.patch \
           file://respect-cflags.patch \
           file://fix-llvm-paths.patch \
           file://install-gbe.patch \
           file://correct-paths.patch \
           file://verbose-make.patch"
SRC_URI_append_class-native = " file://reduced-native.patch"

BBCLASSEXTEND = "native"
# TODO set PV to reflect that this is 1.1.2+patches.

# Need a newer srcrev as 1.1.2 won't build with the LLVM release in meta-clang
SRCREV = "c511a8f051a6efa57fa28314a7b5f51b658afa59"
S = "${WORKDIR}/git"

DEPENDS = "beignet-native clang ocl-icd libdrm mesa"
DEPENDS_class-native = "clang-native"

inherit cmake pkgconfig

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE = "-DCMAKE_SKIP_RPATH=TRUE -DLLVM_LIBRARY_DIR=${STAGING_LIBDIR} -DBEIGNET_INSTALL_DIR=/usr/lib/beignet"
EXTRA_OECMAKE_append_class-target = " -DUSE_STANDALONE_GBE_COMPILER=true"
#EXTRA_OECMAKE_append_class-target = " -DGEN_PCI_ID=0x0166"

# TODO respect distrofeatures for x11
PACKAGECONFIG ??= ""
PACKAGECONFIG[examples] = '-DBUILD_EXAMPLES=1,-DBUILD_EXAMPLES=0,libva'
# TODO: add explicit on/off upstream
PACKAGECONFIG[x11] = ",,libxext libxfixes"

FILES_${PN} += " \
                ${sysconfdir}/OpenCL/vendors/intel-beignet.icd \
                ${libdir} ${includedir} \
               "

FILES_${PN}-dev = ""

do_install_append() {
    # Remove the headers; these will be included by another recipe
    rm -rf ${D}${includedir}/CL

    # Create intel-beignet.icd file
    mkdir -p ${D}${sysconfdir}/OpenCL/vendors/
    echo ${libdir}/beignet/libcl.so > ${D}${sysconfdir}/OpenCL/vendors/intel-beignet.icd
}

do_install_append_class-target() {
    # Sanity test that the build was actually successful
    test -f ${D}${libdir}/beignet/beignet.bc || bberror "beignet.bc wasn't built"

    install -D -m 0755 ${B}/backend/src/gbe_bin_generater ${D}${bindir}/gbe_bin_generater

    # Install pieces so the test suite can run
    install -d ${D}${libdir}/beignet/utests
    install -m 0755 ${B}/utests/utest_run ${D}${libdir}/beignet/utests
    install -m 0755 ${B}/utests/setenv.sh ${D}${libdir}/beignet/utests
    install -m 0644 ${S}/kernels/*.cl ${D}${libdir}/beignet/utests
    install -m 0644 ${S}/kernels/*.bmp ${D}${libdir}/beignet/utests
    install -m 0644 ${S}/kernels/compiler_ceil.bin ${D}${libdir}/beignet/utests
    install -m 0644 ${S}/kernels/runtime_compile_link.h ${D}${libdir}/beignet/utests
    install -m 0644 ${S}/kernels/include/runtime_compile_link_inc.h ${D}${libdir}/beignet/utests/include
    install -m 0644 ${B}/utests/libutests.so ${D}${libdir}
}

do_install_class-native() {
    install -d ${D}${libdir}/cmake
    install -m644 ${S}/CMake/FindStandaloneGbeCompiler.cmake ${D}${libdir}/cmake

    install -d ${D}${bindir}
    install ${B}/backend/src/gbe_bin_generater ${D}${bindir}
    install ${B}/backend/src/libgbe.so ${D}${libdir}

    install -d ${D}${bindir}/include
    install ${B}/backend/src/libocl/usr/lib/beignet/include/* ${D}${bindir}/include
}
