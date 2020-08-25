# Jailhouse support layer

Yocto layer that enables use of the Jailhouse partitioning hypervisor - <https://github.com/siemens/jailhouse>.

## How to use

To enable build of the Jailhouse hypervisor, the layer has to be added to the Yocto setup:

    bitbake-layers add-layer meta-jailhouse

After that, this configuration fragment has to be added into conf/local.conf:

    ENABLE_JAILHOUSE = "1"
    require conf/jailhouse.conf

That will enable this layer and include the `jailhouse` package in the image.

Then, in the target system, the cell configurations (*.cell) are placed in `/usr/share/jailhouse/cells/` and the demo inmates (bare-metal applications to run in a non-root cell) are located in `/usr/share/jailhouse/inmates`.

## Raspberry Pi 4 example

Use this commands to enable Jailhouse and run the GIC demo inmate in a non-root cell. After issuing these commands, the GIC demo will be mesauring jitter of a timer and print the output on the serial console of the RPi.

    jailhouse enable /usr/share/jailhouse/cells/rpi4.cell
    jailhouse cell create /usr/share/jailhouse/cells/rpi4-inmate-demo.cell
    jailhouse cell load inmate-demo /usr/share/jailhouse/inmates/gic-demo.bin
    jailhouse cell start inmate-demo

## Dependencies

This layer depends on:

* URI: git://git.yoctoproject.org/meta-arm
  * branch: master
  * revision: 2f3898a46e17d55b7097496d080188bf8fea7821
  * note: actually only required on the Raspberry Pi 4 target

## Supported targets

* Raspberry Pi 4
    * All (1G-8G) memory variants. But note there is 256M reserved for Jailhouse and at least 64 MiB for the GPU (configurable), so the variants with more memory are recommended.

* QEMU x86-64
    * Work in progress. Requires KVM. Nested virtualization must be enabled on the host. Currently, the right configuration of QEMU and Jailhouse to work out-of-box is being worked on.
