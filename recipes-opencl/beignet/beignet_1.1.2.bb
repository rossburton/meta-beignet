LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6b566c5b4da35d474758324899cb4562"

SRC_URI = "git://anongit.freedesktop.org/beignet \
           file://respect-cflags.patch \
           file://fix-llvm-paths.patch"
SRC_URI_append_class-native = " file://reduced-native.patch"

BBCLASSEXTEND = "native"
# TODO set PV to reflect that this is 1.1.2+patches.

# Need a newer srcrev as 1.1.2 won't build with the LLVM release in meta-clang
SRCREV = "8dfec54e2f3e32710702ed60f5171741360f28bb"
S = "${WORKDIR}/git"

DEPENDS = "beignet-native clang ocl-icd libdrm mesa"
DEPENDS_class-native = "clang-native"

inherit cmake pkgconfig

export LLVM_HACK = "${STAGING_LIBDIR}"

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE = " -DUSE_STANDALONE_GBE_COMPILER=true -DSTANDALONE_GBE_COMPILER_DIR=${STAGING_NATIVE_BINDIR}"
EXTRA_OECMAKE_class-native = ""

# TODO respect distrofeatures for x11
PACKAGECONFIG ??= ""
PACKAGECONFIG[examples] = '-DBUILD_EXAMPLES=1,-DBUILD_EXAMPLES=0,libva'
# TODO: add explicit on/off upstream
PACKAGECONFIG[x11] = ",,libxext libxfixes"

FILES_${PN} += " \
                ${sysconfdir}/OpenCL/vendors/intel-beignet.icd \
                ${libdir} \
                ${libdir}/beignet/ \
                ${libdir}/beignet/* \
               "

do_install_append () {
    # Remove the headers; these will be included by another recipe
    rm -rf ${D}${includedir}/CL

    # Create intel-beignet.icd file
    mkdir -p ${D}${sysconfdir}/OpenCL/vendors/
    echo ${libdir}/beignet/libcl.so > ${D}${sysconfdir}/OpenCL/vendors/intel-beignet.icd
}

do_install_class-native() {
    install -d ${D}${libdir}/cmake
    install -m644 ${S}/CMake/FindStandaloneGbeCompiler.cmake ${D}${libdir}/cmake

    install -d ${D}${bindir}
    install ${B}/backend/src/gbe_bin_generater ${D}${bindir}
}
