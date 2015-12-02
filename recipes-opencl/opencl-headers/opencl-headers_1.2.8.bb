LICENSE = "opencl-headers"
LIC_FILES_CHKSUM = "file://cl.h;beginline=1;endline=22;md5=688fcb59ec225081cedfaef77315908f"

S = "${WORKDIR}"

# This is really tedious but Khronos supplies each file individually
SRC_URI = "https://www.khronos.org/registry/cl/api/1.2/cl.h;name=cl_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_d3d10.h;name=cl_d3d10_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_d3d11.h;name=cl_d3d11_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_dx9_media_sharing.h;name=cl_dx9_media_sharing_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_egl.h;name=cl_egl_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_ext.h;name=cl_ext_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_gl.h;name=cl_gl_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_gl_ext.h;name=cl_gl_ext_h \
           https://www.khronos.org/registry/cl/api/1.2/cl_platform.h;name=cl_platform_h \
           https://www.khronos.org/registry/cl/api/1.2/opencl.h;name=opencl_h \
           https://www.khronos.org/registry/cl/api/2.1/cl.hpp;name=cl_hpp \
          "

SRC_URI[cl_h.md5sum] = "393ecb00c9a15a2a942e135fd4eb4b82"
SRC_URI[cl_h.sha256sum] = "ee0bde19ac49607855d3f5025c6932ccbeed420291dfb9080690a4cfa0c12901"
SRC_URI[cl_d3d10_h.md5sum] = "733d5d6b54cebdd0ecdde27e341bd465"
SRC_URI[cl_d3d10_h.sha256sum] = "55b90d14c388f1b8b93e04a549fa3a25c74f7476ffa15a71e17322bc365b4363"
SRC_URI[cl_d3d11_h.md5sum] = "f53b2ffef7d9197fcc3cf80df2059d35"
SRC_URI[cl_d3d11_h.sha256sum] = "ab036bbcb6ee11aad3038d33c485a9bcf2a15eb6eb83b9a4a9bd72fb98ce3360"
SRC_URI[cl_dx9_media_sharing_h.md5sum] = "1ad86f41fd01f3ba0388f5b610e65a07"
SRC_URI[cl_dx9_media_sharing_h.sha256sum] = "774bede18a9f26827c4c05a1ee591a5c607f4ef153ad799c47efdb44727d256c"
SRC_URI[cl_egl_h.md5sum] = "b58aebb229378ac0c8d076828ea37a24"
SRC_URI[cl_egl_h.sha256sum] = "31e046f50c34b24ddb9dd2a9eb182c854703c862c5e10f0145175567d6b6e670"
SRC_URI[cl_ext_h.md5sum] = "d5630fb0dc6fb6e9f3b679f26a80a075"
SRC_URI[cl_ext_h.sha256sum] = "23b4a6227b3ac59c72518d9abe05b56e427b2f302ec24dec32023b9ef5fb33d0"
SRC_URI[cl_gl_h.md5sum] = "b8429948c35e43d72f944a4d732967e5"
SRC_URI[cl_gl_h.sha256sum] = "0b53a3b04bbcd6c122acc0fe9ccdacf80d521a64bb29c02cddad60a4ea447bf8"
SRC_URI[cl_gl_ext_h.md5sum] = "9e389c6edecc8559ca9b861ed3e8e96b"
SRC_URI[cl_gl_ext_h.sha256sum] = "4d663bbfc932ab405623e5f5d1bba3f438351d0ead65e1ccdd9bb769caf7a7d0"
SRC_URI[cl_platform_h.md5sum] = "360ac18b454f86e93a63afda1c3061e2"
SRC_URI[cl_platform_h.sha256sum] = "f196eb389a5030e67d176157081f09ab7ab97dcfdd94345ee8d2055496fa24b8"
SRC_URI[opencl_h.md5sum] = "6f511443ae9d2f85146e0c35221c1e7d"
SRC_URI[opencl_h.sha256sum] = "be1bf717e103678292d6718b3608c9e54a48588c02d3d387a48656fac46ec8d9"
SRC_URI[cl_hpp.md5sum] = "f2c8bee05e5a84ea8282b7b95646c515"
SRC_URI[cl_hpp.sha256sum] = "08034743b513512bb6ea3a1e9a59bdf1842d8199fccad73ac1b406a88d60f7e0"

CLDIR = "${includedir}/CL"

FILES_${PN} = " \
               ${CLDIR} \
               ${CLDIR}/* \
              "

FILES_${PN}-dev = ""
ALLOW_EMPTY_${PN}-dev = "1"


do_configure () {
}

do_compile () {
}

do_install () {
    mkdir -p ${D}${CLDIR}

    cd ${WORKDIR}
    cp cl.h ${D}${CLDIR}/
    cp cl_d3d10.h ${D}${CLDIR}/
    cp cl_d3d11.h ${D}${CLDIR}/
    cp cl_dx9_media_sharing.h ${D}${CLDIR}/
    cp cl_egl.h ${D}${CLDIR}/
    cp cl_ext.h ${D}${CLDIR}/
    cp cl_gl.h ${D}${CLDIR}/
    cp cl_gl_ext.h ${D}${CLDIR}/
    cp cl_platform.h ${D}${CLDIR}/
    cp opencl.h ${D}${CLDIR}/
    cp cl.hpp ${D}${CLDIR}/
}
