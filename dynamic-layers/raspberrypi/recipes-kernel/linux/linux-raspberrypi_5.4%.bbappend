FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

TOADD := "${@bb.utils.contains('ENABLE_JAILHOUSE', '1', 'file://0001-dt-dtoverlays-Add-jailhouse-memory-DT-overlay.patch', '', d)}"
SRC_URI += "${TOADD}"

require ${@bb.utils.contains('ENABLE_JAILHOUSE', '1', 'recipes-kernel/linux/linux-jailhouse-5.4.inc', '', d)}
