include beignet_${PV}.bb

# Bay Trail's GPU PCI ID is 0x0f31
EXTRA_OECMAKE += " -DGEN_PCI_ID=0x0f31 "
