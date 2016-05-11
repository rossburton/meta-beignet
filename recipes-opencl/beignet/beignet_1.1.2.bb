# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)
#
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6b566c5b4da35d474758324899cb4562"

SRC_URI = "git://anongit.freedesktop.org/beignet"
SRCREV = "8dfec54e2f3e32710702ed60f5171741360f28bb"
S = "${WORKDIR}/git"

#SRC_URI[md5sum] = "02d95e967100ca1081298ef7ec32797f"
#SRC_URI[sha256sum] = "6a8d875afbb5e3c4fc57da1ea80f79abadd9136bfd87ab1f83c02784659f1d96"
#S = "${WORKDIR}/Beignet-1.1.2-Source"

DEPENDS = "ocl-icd clang libva libdrm libxext libxfixes zlib mesa"

#TOOLCHAIN = "clang"
#export PYTHON_EXECUTABLE = "python"

inherit cmake pkgconfig

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"
EXTRA_OECMAKE = " \
        -DCMAKE_BUILD_TYPE=Release \
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

do_install_append () {
    # Remove the headers; these will be included by another recipe
    rm -rf ${D}${includedir}/CL

    # Create intel-beignet.icd file
    mkdir -p ${D}${sysconfdir}/OpenCL/vendors/
    echo ${libdir}/beignet/libcl.so > ${D}${sysconfdir}/OpenCL/vendors/intel-beignet.icd
}

#INSANE_SKIP_${PN} = "debug-files"
