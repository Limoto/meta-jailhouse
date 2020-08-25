
require ${@bb.utils.contains('ENABLE_JAILHOUSE', '1', 'recipes-kernel/linux/linux-jailhouse-5.4.inc', '', d)} 

