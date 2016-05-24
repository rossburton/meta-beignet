# TODO For now this is a packaging of the binaries.

SUMMARY = "OpenCL benchmarking tool"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://luxmark.bin;md5=c2ccd180718be1a7acf9763217bbcc56"

DEPENDS = "mesa libx11 fontconfig ocl-icd mesa-glut libxrender"

SRC_URI = "http://www.luxrender.net/release/luxmark/v${PV}/luxmark-linux64-v${PV}.tar.bz2"
SRC_URI[md5sum] = "3d96191f9dfd6a91f2866c309195d991"
SRC_URI[sha256sum] = "41756405ca55cd2c3be26bc4c95201960694ff1b28909f4c6fc54e2af9a8ca72"

S = "${WORKDIR}/${PN}-v${PV}"

do_install() {
	mkdir --parents ${D}/opt
	cp -R ${S} ${D}/opt/

	# The binary expects the loader in /lib64 so be evil and add a
	# symlink if it isn't there.
	if [ ${base_libdir} != "/lib64" ]; then
		ln -s ${base_libdir} ${D}/lib64
	fi
}

DEBIAN_NAMES = "0"

FILES_${PN} = "/opt /lib64"
RDEPENDS_${PN} += "bash"

INSANE_SKIP_${PN} = "ldflags dev-so"
