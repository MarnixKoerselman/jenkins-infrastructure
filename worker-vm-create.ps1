# https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/user-guide/nested-virtualization

# find the VM
Get-VM
$MyHypervHostVMName = "Jenkins Worker on Windows Server 2019"

# stop the VM
Stop-VM $MyHypervHostVMName

# execute this
Set-VMProcessor -VMName $MyHypervHostVMName -ExposeVirtualizationExtensions $true
Set-VMNetworkAdapter -VMName $MyHypervHostVMName -MacAddressSpoofing On
# stop using dynamic memory
Set-VMMemory $MyHypervHostVMName -DynamicMemoryEnabled $false -StartupBytes 4GB

# the Jenkins worker VM should use the default switch, which allows access to the outside world while being protected by the  default Windows NAT

# allow echo requests to test the client VM, by enabling the "echo request" firewall rules for IPv4 and v6
