SUMMARY = "Linux-based partitioning hypervisor"
DESCRIPTION = "Jailhouse is a partitioning Hypervisor based on Linux. It is able to run bare-metal applications or (adapted) \
operating systems besides Linux. For this purpose, it configures CPU and device virtualization features of the hardware \
platform in a way that none of these domains, called 'cells' here, can interfere with each other in an unacceptable way."
HOMEPAGE = "https://github.com/siemens/jailhouse"
SECTION = "jailhouse"
LICENSE = "GPL-2.0 & BSD-2-Clause"

LIC_FILES_CHKSUM = " \
    file://COPYING;md5=9fa7f895f96bde2d47fd5b7d95b6ba4d \
"

SRCREV = "4ce7658dddfd5a1682a379d5ac46657e93fe1ff0"

PV = "0.12+git${SRCPV}"

SRC_URI = " \
    git://github.com/siemens/jailhouse \
    file://0001-tools-update-shebang-in-helper-scripts-for-python3.patch \
"

DEPENDS = "virtual/kernel dtc-native python3-mako-native python3-mako make-native"

DEPENDS_append_raspberrypi4 = " trusted-firmware-a"

RDEPENDS_${PN} += "\
	python3-curses\
	python3-datetime\
	python3-mmap\
"

require jailhouse-arch.inc
inherit module python3native bash-completion setuptools3

S = "${WORKDIR}/git"
B = "${S}"

JH_DATADIR ?= "${datadir}/jailhouse"
JH_EXEC_DIR ?= "${libexecdir}/jailhouse"
CELL_DIR ?= "${JH_DATADIR}/cells"
CELLCONF_DIR ?= "${JH_DATADIR}/configs"
INMATES_DIR ?= "${JH_DATADIR}/inmates"

JH_CELL_FILES ?= "*.cell"

do_configure() {
	if [ -d ${STAGING_DIR_HOST}/${CELLCONF_DIR} ]; 
	then
		cp ${STAGING_DIR_HOST}/${CELLCONF_DIR}/*.c ${S}/configs/
	fi
}

#USER_SPACE_CFLAGS = '${CFLAGS} -DLIBEXECDIR=\\\"${libexecdir}\\\" \
#                    -DJAILHOUSE_VERSION=\\\"$JAILHOUSE_VERSION\\\" \
#                    -Wall -Wextra -Wmissing-declarations -Wmissing-prototypes -Werror \
#                    -I../driver'

TOOLS_SRC_DIR = "${S}/tools"

EXTRA_OEMAKE = "ARCH=${JH_ARCH} CROSS_COMPILE=${TARGET_PREFIX} CC="${CC}" KDIR=${STAGING_KERNEL_BUILDDIR}"

do_compile() {
	oe_runmake V=1
}

do_install() {
	# Install pyjailhouse python modules needed by the tools
	distutils3_do_install

	# We want to install the python tools, but we do not want to use pip...
	# At least with v0.10, we can work around this with
	# 'PIP=":" PYTHON_PIP_USEABLE=yes'
	oe_runmake PIP=: PYTHON=python3 PYTHON_PIP_USEABLE=yes DESTDIR=${D} install

	install -d ${D}${CELL_DIR}
	install -m 0644 ${B}/configs/${JH_ARCH}/${JH_CELL_FILES} ${D}${CELL_DIR}/

	install -d ${D}${INMATES_DIR}
	install -m 0644 ${B}/inmates/demos/${JH_ARCH}/*.bin ${D}${INMATES_DIR}

}

PACKAGE_BEFORE_PN = "kernel-module-jailhouse pyjailhouse ${PN}-tools"
FILES_${PN} = "${base_libdir}/firmware ${libexecdir} ${sbindir} ${JH_DATADIR}"
FILES_pyjailhouse = "${PYTHON_SITEPACKAGES_DIR}"
FILES_${PN}-tools = "${libexecdir}/${BPN}/${BPN}-*"

RDEPENDS_${PN}-tools = "pyjailhouse python3-mmap python3-math python3-datetime python3-curses python3-compression"
RDEPENDS_pyjailhouse = "python3-core python3-ctypes python3-fcntl python3-shell"

RRECOMMENDS_${PN} = "${PN}-tools"

#INSANE_SKIP_${PN} = "ldflags"

KERNEL_MODULE_AUTOLOAD += "jailhouse"

# Any extra cells/inmates from external recipes/packages
CELLS = ""

python __anonymous () {
    # Setup DEPENDS and RDEPENDS to included cells
    cells = d.getVar('CELLS', True) or ""
    for cell in cells.split():
        d.appendVar('DEPENDS', ' ' + cell)
        d.appendVar('RDEPENDS_${PN}', ' ' + cell)
}