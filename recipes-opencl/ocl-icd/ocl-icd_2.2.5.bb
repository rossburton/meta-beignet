LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=8642113c8649caa37fbc9f8200c31fd3"

SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/ocl-icd/ocl-icd-${PV}.tar.gz/a99017db1f8f4297e779bdc94daba4e0/ocl-icd-${PV}.tar.gz"
SRC_URI[md5sum] = "a99017db1f8f4297e779bdc94daba4e0"
SRC_URI[sha256sum] = "81114cb04cf49a7f6f629bba1765e24eee8d968e0d9cbd537adad3705785013e"

inherit autotools

DEPENDS += " ruby-native "

FILES_${PN} += "${datadir}/COPYING"

do_install_append () {
    # OpenCL headers to be included by another recipe
    rm -rf ${D}${includedir}/CL/

    # COPYING file says to distrubute the copyright file. Do just that.
    mkdir -p ${D}${datadir}/${PN}/
    cp ${S}/COPYING ${D}${datadir}/${PN}/
}
