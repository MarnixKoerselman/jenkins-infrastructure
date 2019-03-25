# https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/user-guide/nested-virtualization

# find the VM
Get-VM
$MyHypervHostVMName = "Jenkins Worker on Windows Server 2019"

# stop the VM
Stop-VM $MyHypervHostVMName

# make the VM capable of being a Hyper-V host
Set-VMProcessor -VMName $MyHypervHostVMName -ExposeVirtualizationExtensions $true
# To use DHCP for IP assignment on a virtual container host enable MACAddressSpoofing
Set-VMNetworkAdapter -VMName $MyHypervHostVMName -MacAddressSpoofing On
# stop using dynamic memory
Set-VMMemory $MyHypervHostVMName -DynamicMemoryEnabled $false -StartupBytes 4GB

# the Jenkins worker VM could use the default switch, which allows access to the outside world while being protected by the  default Windows NAT
# but on the other hand, if the host changes location a lot (between office and home) then the worker VM gets a new network quite often;
# in that case a custom nat (e.g. DEVOPSNAT) could be preferable. Use docker so that the same network may be used by docker containers and Hyper-V VMs.
$networkName = 'DEVOPSNAT'
docker network rm $networkName
docker network create -d nat --subnet=192.168.197.0/24 --gateway=192.168.197.1 --opt com.docker.network.windowsshim.dnsservers=8.8.8.8 --opt com.docker.network.windowsshim.networkname=$networkName $networkName
# NB com.docker.network.windowsshim options are described here: https://godoc.org/github.com/docker/libnetwork/drivers/windows
#  and here: https://docs.microsoft.com/en-us/virtualization/windowscontainers/container-networking/advanced

# allow echo requests to test the client VM, by enabling the "echo request" firewall rules for IPv4 and v6
