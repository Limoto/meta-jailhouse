BBLAYERS =+ " \
   ${METADIR}/meta-jailhouse/meta-agl-jailhouse \
"


BBLAYERS =+ "${@ '${METADIR}/meta-jailhouse/meta-agl-jailhouse-raspberrypi ${METADIR}/meta-arm/meta-arm' if d.getVar('MACHINE') == 'raspberrypi4' else ''}"

