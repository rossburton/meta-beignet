LICENSE = "PublicDomain"
LIC_FILES_CHKSUM = "file://LICENSE;md5=069146d1c0028c0ef02b59ad670eec54"

SRC_URI = "https://github.com/Oblomov/${BPN}/archive/${PV}.tar.gz"
SRC_URI[md5sum] = "6c7abd0fda5d546982adeb622b0650ed"
SRC_URI[sha256sum] = "f92fc60f337ad86c8506d7d03358bf47980cb08fca1a0ca496b15282db59dea3"

DEPENDS = "ocl-icd"

do_install () {
	install -D -m755 clinfo ${D}${bindir}/clinfo
	install -D -m644 man/clinfo.1 ${D}${mandir}/man1/clinfo.1
}
