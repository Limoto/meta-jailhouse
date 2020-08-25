
do_deploy_append_raspberrypi4() {
    if ${@bb.utils.contains('ENABLE_JAILHOUSE', '1', 'true', 'false', d)}; then

        # if ARMSTUB is set, it should be set in config.txt by earlier recipes, so replace it
        if ${@bb.utils.contains("MACHINE_FEATURES", "armstub", "true", "false", d)}; then
            sed -i 's/^armstub=.*/armstub=bl31.bin/' ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt

            if ! grep '^enable_gic' config.txt; then
                    sed -i 's/^enable_gic=.*/enable_gic=1/' ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
            else
                echo "enable_gic=1" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
            fi	
        
        # otherwise, set it
        else
            echo "# ARM stub configuration" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
            echo "armstub=bl31.bin" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
            echo "enable_gic=1" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
        fi

        # memory reserved for Jailhouse
        echo "dtoverlay=jailhouse-memory" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt
        echo "dtoverlay=jailhouse-memory,start=0xe0000000,size=0x200000" >> ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/config.txt

   fi
}


