# move to linux-agl.inc or the like
FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"
SRC_URI_append = " file://jailhouse.cfg"

