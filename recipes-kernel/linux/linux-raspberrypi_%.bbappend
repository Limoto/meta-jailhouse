# move to linux-agl.inc or the like
FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"
SRC_URI_append = " file://jailhouse.cfg"

# required for Jailhouse to work with the supplied cell confiugrations
CMDLINE_append = " mem=768M"