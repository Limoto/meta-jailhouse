COMPATIBLE_MACHINE = "raspberrypi4"
TFA_BUILD_TARGET = "bl31"
TFA_PLATFORM = "rpi4"

# Skip installing the binary into /lib/firmware. We only need it on the boot
# partition that is generated from the files in DEPLOYDIR
do_install[noexec] = "1"

FILES_${PN} = ""

do_deploy() {
    install -d ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}
    cp ${BUILD_DIR}/bl31.bin ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/bl31.bin
}

