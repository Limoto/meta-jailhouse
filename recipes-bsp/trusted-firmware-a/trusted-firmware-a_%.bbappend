COMPATIBLE_MACHINE_raspberrypi4 = "raspberrypi4"
TFA_BUILD_TARGET_raspberrypi4 = "bl31"
TFA_PLATFORM_raspberrypi4 ?= "rpi4"

do_install() {    
}

FILES_${PN} = ""

#TODO how to unappend this?
SYSROOT_DIRS += "/boot"

do_deploy() {
    install -d ${DEPLOYDIR}/bcm2835-bootfiles
    cp ${BUILD_DIR}/bl31.bin ${DEPLOYDIR}/bcm2835-bootfiles/bl31.bin
}