import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.util.Arrays;
import java.util.List;

/**
 * This is a simple example of how to get started with Structurizr for Java.
 */
public class Structurizr {

    private static final long WORKSPACE_ID = 43169;
    private static final String API_KEY = "291a1d66-190a-40c3-9b5b-d7cacdf3970b";
    private static final String API_SECRET = "8409e41f-5de6-4386-a01a-f160314b5c11";

    public class MyTags extends Tags {
        public static final String PROCESS = "Process";
        public static final String REPOSITORY = "Repository";
        public static final String JENKINS_MASTER = "Mr Jenkins";
        public static final String JENKINS_EXECUTOR = "Jenkins Executor";
        public static final String JENKINS_AGENT = "Jenkins Agent";
    }

    public static void main(String[] args) throws Exception {
        // a Structurizr workspace is the wrapper for a software architecture model, views and documentation
        Workspace workspace = new Workspace("CI / CD", "Continuous Integration and Continuous Delivery with Jenkins and Docker.");

        //makeSimpleSystem(workspace);
        makeComplexSystem(workspace);

        //--------------------
        // add some styling
        ViewSet views = workspace.getViews();
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
        styles.addElementStyle(Tags.CONTAINER).background("#438dd5");
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);

        styles.addElementStyle(MyTags.PROCESS).shape(Shape.Ellipse).border(Border.Dashed);
        styles.addElementStyle(MyTags.REPOSITORY).shape(Shape.Cylinder).background("#08a07b");
        styles.addElementStyle(MyTags.JENKINS_MASTER).setShape(Shape.Robot);
        styles.addElementStyle(MyTags.JENKINS_AGENT).setShape(Shape.RoundedBox);
        styles.addElementStyle(MyTags.JENKINS_EXECUTOR).setShape(Shape.Circle);

        uploadWorkspaceToStructurizr(workspace);
    }

    private static void makeComplexSystem(Workspace workspace) {
        Model model = workspace.getModel();

        // static model
        Person developer = model.addPerson(Location.Internal, "Software developer", "Persona that works on source code, project configuration and build/test/deploy scripts.");
        Person releaseManager = model.addPerson("Release manager", "Persona that manages the public release of artifacts.");
        Person jenkinsAdmin = model.addPerson(Location.Internal, "Jenkins administrator", "Shared role. Maintenance of the CI/CD ecosystem. This includes feeding the internal Docker image repository with copies from the external Docker image repositories.");

        SoftwareSystem continuousIntegrationSoftwareSystem = model.addSoftwareSystem(Location.Internal, "DTN WS CI/CD", "Continuous Integration and Continuous Delivery system.");
        SoftwareSystem dockerImageRepoMS = model.addSoftwareSystem(Location.External, "mcr.microsoft.com", "Microsoft's docker image repository.");
        SoftwareSystem dockerImageRepo = model.addSoftwareSystem(Location.External, "hub.docker.com", "Docker hub image repository.");

        Container jenkinsMasterContainer = continuousIntegrationSoftwareSystem.addContainer(
                "Jenkins master",
                "The process controller",
                "docker or VM, Linux");
        Container jenkinsBuildAgentContainer = continuousIntegrationSoftwareSystem.addContainer(
                "Jenkins build agent",
                "A computing node that runs one or more build executors, depending on (hardware) resources. A build agent employs docker containers to run specific build steps, as defined in a Jenkinsfile. Therefore the build agent must be connected to a Docker host capable of running these containers.",
                "VM, Windows");
        Container artifactRepositoryContainer = continuousIntegrationSoftwareSystem.addContainer(
                "Artifact repository system",
                "Artifact repository, for use by both internal processes (build artifacts) as external processes (deployment on customer machine, in the field).",
                "Windows? Linux? depends on choice for the system. E.g. Nexus should be run on Linux.");
        Container jenkinsHardwareTestAgentContainer = continuousIntegrationSoftwareSystem.addContainer(
                "Jenkins test agent (hardware-dependent)",
                "A computing node that has hardware to make all integration tests possible (including those that depend on the availability of a sound card).",
                "Windows");
        Container codeRepositoryContainer = continuousIntegrationSoftwareSystem.addContainer(
                "Code repository",
                "SCM.",
                "hardware, Linux");
        Container jenkinsUnitTestAgentContainer = continuousIntegrationSoftwareSystem.addContainer(
                "Jenkins test agent (unit)",
                "A computing node that runs a Jenkins test agent for hardware-independent unit tests.",
                "Windows");
        Container jenkinsIntegrationTestAgentContainer = continuousIntegrationSoftwareSystem.addContainer(
                "Jenkins test agent (integration)",
                "A computing node that runs a Jenkins test agent for hardware-independent integration tests.",
                "Windows");
        Container developmentTools = continuousIntegrationSoftwareSystem.addContainer(
                "Development tools",
                "All the stuff developers install on their laptop.",
                "Windows");

        List<Container> buildStepContainers = Arrays.asList(
                continuousIntegrationSoftwareSystem.addContainer("Docker image builder", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("CPPCHECK version x", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("CPPCHECK version y", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("VS2017-builder", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("VS2015-builder", "build step", "docker, Windows")
        );

        Container jenkinsDockerImageBuilderContainer = buildStepContainers.get(0);

        jenkinsBuildAgentContainer.addComponent("Jenkins swarm client", "Client software that complements the Jenkins Self-Organizing Swarm Plug-in", "Java");

        jenkinsMasterContainer.addComponent("Jenkins Self-Organizing Swarm Plug-in", "Plugin that makes the cloud of agents dynamic", "Jenkins plugin");

        jenkinsUnitTestAgentContainer.addComponent("Jenkins swarm client", "Client software that complements the Jenkins Self-Organizing Swarm Plug-in", "Java");

        jenkinsHardwareTestAgentContainer.addComponent("Jenkins swarm client", "Client software that complements the Jenkins Self-Organizing Swarm Plug-in", "Java");

        jenkinsIntegrationTestAgentContainer.addComponent("Jenkins swarm client", "Client software that complements the Jenkins Self-Organizing Swarm Plug-in", "Java");

        codeRepositoryContainer.addComponent("svn", "Current SCM mechanism", "old");
        codeRepositoryContainer.addComponent("git", "Future SCM mechanism", "modern");

        //------------------------
        // relationships
        developer.uses(continuousIntegrationSoftwareSystem, "Supplies input and starts builds.");
        developer.uses(continuousIntegrationSoftwareSystem, "Observes (and responds to) feedback from the build system.");
        releaseManager.uses(continuousIntegrationSoftwareSystem, "Manages public release of artifacts.");

        // NB: this turns out (in deployment view) as: every BuildAgent uses every xxxTestAgent (even those on different deployment nodes)
//        jenkinsBuildAgentContainer.uses(jenkinsUnitTestAgentContainer, "A build agent uses test agents to run (hardware dependent) tests, if specified in the Jenkinsfile.");
//        jenkinsBuildAgentContainer.uses(jenkinsHardwareTestAgentContainer, "A build agent uses test agents to run (hardware dependent) tests, if specified in the Jenkinsfile.");
//        jenkinsDockerImageBuilderContainer.uses(artifactRepositoryContainer, "Images for build steps are stored in the artifact repository for easy access and for posterity (so we can perform the same build in about 20 years from now).", "https", InteractionStyle.Synchronous);

        artifactRepositoryContainer.delivers(releaseManager, "The release manager receives the final product from the artifact repository", "https", InteractionStyle.Asynchronous);

        jenkinsAdmin.uses(artifactRepositoryContainer, "Store copies (i.e. 'cache') Docker images from external sources (e.g. Microsoft) to enable rebuilding build step images in about 20 years from now.");
        jenkinsAdmin.uses(dockerImageRepo, "Download docker images from external source for use in our internal build system.");
        jenkinsAdmin.uses(dockerImageRepoMS, "Download docker images from external source for use in our internal build system. NB: this may not be allowed (MS license) in which case the build step images must be stored for eternity.");

        jenkinsMasterContainer.uses(jenkinsBuildAgentContainer, "Delegate setting up the build context and running the build scripts.");

        //----------------------
        // deployment model
        String sDtnInternalNetworkEnvironment = "DTN internal network";
        DeploymentNode jenkinsPrimeNode = model.addDeploymentNode(sDtnInternalNetworkEnvironment,
                "Jenkins Prime",
                "Beefy host machine for the Jenkins master, which has enough resources to also host at least one Jenkins build agent",
                "hardware, Windows Server 2019");
        DeploymentNode primeHyperVHost = jenkinsPrimeNode.addDeploymentNode(
                "Hyper-V hypervisor",
                "Hyper-V hypervisor that comes with Windows Server",
                "Hyper-V");
        DeploymentNode primeWindowsHost = primeHyperVHost.addDeploymentNode(
                "Windows Server 2019",
                "Windows Server 2019",
                "hardware, Windows");
        DeploymentNode primeWindowsVM = primeHyperVHost.addDeploymentNode(
                "Windows Virtual Machine (WS2019)",
                "A 'standard' Virtual Machine that developers can run on on their laptops. The VM automatically connects with Docker master and adds itself as a build agent. Builds are typically implemented as build steps using Docker images.",
                "Hyper-V, Windows");
        DeploymentNode primeDockerHost = primeWindowsVM.addDeploymentNode(
                "Windows Docker host",
                "Docker host for Windows containers",
                "docker");
        DeploymentNode primeLinuxVM = primeHyperVHost.addDeploymentNode(
                "Linux Virtual Machine",
                "Linux host for Linux docker containers... maybe this will instead be a docker-machine (deployment node) under or next to the prime Docker host deployment node",
                "Hyper-V, Linux");
        DeploymentNode primeLinuxDockerHost = primeLinuxVM.addDeploymentNode(
                "Linux Docker host",
                "Host for Linux-based docker images",
                "docker");

        DeploymentNode dynamicNode = model.addDeploymentNode(sDtnInternalNetworkEnvironment, "Laptop", "Developer's laptop", "hardware, Windows", 3);
        DeploymentNode dynamicHyperVHost = dynamicNode.addDeploymentNode(
                "Hyper-V hypervisor",
                "Hyper-V hypervisor that comes with Windows 10",
                "Hyper-V");
        DeploymentNode dynamicHostOS = dynamicHyperVHost.addDeploymentNode(
                "Windows 10",
                "Host OS on a development laptop",
                "Windows");
        DeploymentNode dynamicWindowsVM = dynamicHyperVHost.addDeploymentNode(
                "Windows Virtual Machine (WS2019)",
                "A 'standard' Virtual Machine that developers can run on on their laptops. The VM automatically connects with Docker master and adds itself as a build agent. Builds are typically implemented as build steps using Docker images.",
                "Hyper-V, Windows");
        DeploymentNode dynamicDockerHost = dynamicWindowsVM.addDeploymentNode(
                "Windows Docker host",
                "Docker host for Windows containers. Builds are typically implemented as build steps using Docker images.",
                "docker");

        DeploymentNode jenkinsHardwareTestMachine = model.addDeploymentNode(sDtnInternalNetworkEnvironment,
                "Jenkins hardware test machine",
                "Host for hardware-dependent tests",
                "hardware");
        DeploymentNode jenkinsHardwareTestOS = jenkinsHardwareTestMachine.addDeploymentNode(
                "Windows 10",
                "Shouldn't this be the same OS that we're targeting in production?",
                "Windows");

        DeploymentNode scmHost = model.addDeploymentNode(sDtnInternalNetworkEnvironment,
                "Repository Host",
                "Host machine for source code repositories",
                "hardware",
                1); // redundancy?

//        DeploymentNode jenkinsOldNode = model.addDeploymentNode(sDtnInternalNetworkEnvironment, "10.167.221.8 (jenkins.dtn.com)", "Current Jenkins machine.", "hardware", 1);
//        DeploymentNode jenkinsOldOS = jenkinsOldNode.addDeploymentNode( "Windows 8.1", "Host OS for current Jenkins machine.", "Windows");
//
//        DeploymentNode robotRunnerTestMachine = model.addDeploymentNode(sDtnInternalNetworkEnvironment, "10.167.221.6 (Robot runner)", "Host for hardware-dependent tests; contains Robot Framework (https://robotframework.org/).", "hardware");
//        DeploymentNode robotRunnerTestOS = robotRunnerTestMachine.addDeploymentNode( "Windows 10", "Shouldn't this be the same OS that we're targeting in production?", "Windows");
//
//        jenkinsOldOS.add(jenkinsMasterContainer);
//        jenkinsOldOS.add(jenkinsBuildAgentContainer);
//        jenkinsOldOS.add(jenkinsUnitTestAgentContainer);
//        jenkinsOldOS.add(jenkinsHardwareTestAgentContainer);
//        jenkinsOldOS.add(jenkinsIntegrationTestAgentContainer);

        primeLinuxDockerHost.add(jenkinsMasterContainer).addTags(MyTags.JENKINS_MASTER);
        primeWindowsVM.add(jenkinsBuildAgentContainer).addTags(MyTags.JENKINS_AGENT);
        primeWindowsVM.add(jenkinsUnitTestAgentContainer).addTags(MyTags.JENKINS_AGENT);
        primeWindowsVM.add(jenkinsIntegrationTestAgentContainer).addTags(MyTags.JENKINS_AGENT);

        jenkinsHardwareTestOS.add(jenkinsHardwareTestAgentContainer).addTags(MyTags.JENKINS_AGENT);

        for (Container buildStepContainer : buildStepContainers) {
            primeDockerHost.add(buildStepContainer).addTags(MyTags.PROCESS);
            dynamicDockerHost.add(buildStepContainer).addTags(MyTags.PROCESS);
        }

        dynamicWindowsVM.add(jenkinsBuildAgentContainer);
        dynamicWindowsVM.add(jenkinsUnitTestAgentContainer);
        dynamicWindowsVM.add(jenkinsIntegrationTestAgentContainer);
        dynamicHostOS.add(developmentTools);

        scmHost.add(artifactRepositoryContainer).addTags(MyTags.REPOSITORY);
        scmHost.add(codeRepositoryContainer).addTags(MyTags.REPOSITORY);

        //-------------------------------------------------------
        // define some views (the diagrams you would like to see)
        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(continuousIntegrationSoftwareSystem, "SystemContext", "A System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        DeploymentView deploymentView = views.createDeploymentView("Deployment", "A Deployment diagram.");
        deploymentView.addAllDeploymentNodes();

        //--------------------------
        // add some documentation
//        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
//        template.addContextSection(continuousIntegrationSoftwareSystem, Format.Markdown,
//                "Here is some context about the software system...\n" +
//                        "\n" +
//                        "![](embed:SystemContext)");
    }

    private static void makeSimpleSystem(Workspace workspace) {
        Model model = workspace.getModel();

        // static model
        Person developer = model.addPerson(Location.Internal, "Software developer", "Persona that works on source code, project configuration and build/test/deploy scripts.");
        Person releaseManager = model.addPerson("Release manager", "Persona that manages the public release of artifacts.");
        Person jenkinsAdmin = model.addPerson(Location.Internal, "Jenkins administrator", "Shared role. Maintenance of the CI/CD ecosystem. This includes feeding the internal Docker image repository with copies from the external Docker image repositories.");

        SoftwareSystem continuousIntegrationSoftwareSystem = model.addSoftwareSystem(Location.Internal, "DTN WS CI/CD", "Continuous Integration and Continuous Delivery system.");
        SoftwareSystem dockerImageRepoMS = model.addSoftwareSystem(Location.External, "mcr.microsoft.com", "Microsoft's docker image repository.");
        SoftwareSystem dockerImageRepo = model.addSoftwareSystem(Location.External, "hub.docker.com", "Docker hub image repository.");

        Container jenkinsMasterContainer = continuousIntegrationSoftwareSystem.addContainer("Jenkins master","The process controller","docker or VM, Linux");
        Container jenkinsBuildAgentContainer = continuousIntegrationSoftwareSystem.addContainer("Jenkins build agent","A computing node that runs one or more build executors, depending on (hardware) resources. A build agent employs docker containers to run specific build steps, as defined in a Jenkinsfile. Therefore the build agent must be connected to a Docker host capable of running these containers.","VM, Windows");
        Container artifactRepositoryContainer = continuousIntegrationSoftwareSystem.addContainer("Artifact repository system","Artifact repository, for use by both internal processes (build artifacts) as external processes (deployment on customer machine, in the field).","Windows? Linux? depends on choice for the system. E.g. Nexus should be run on Linux.");
        Container jenkinsHardwareTestAgentContainer = continuousIntegrationSoftwareSystem.addContainer("Jenkins test agent (hardware-dependent)","A computing node that has hardware to make all integration tests possible (including those that depend on the availability of a sound card).","Windows");
        Container codeRepositoryContainer = continuousIntegrationSoftwareSystem.addContainer("Code repository","SCM.","hardware, Linux");
        Container jenkinsUnitTestAgentContainer = continuousIntegrationSoftwareSystem.addContainer("Jenkins test agent (unit)","A computing node that runs a Jenkins test agent for hardware-independent unit tests.","Windows");
        Container jenkinsIntegrationTestAgentContainer = continuousIntegrationSoftwareSystem.addContainer("Jenkins test agent (integration)","A computing node that runs a Jenkins test agent for hardware-independent integration tests.","Windows");
        Container developmentToolsContainer = continuousIntegrationSoftwareSystem.addContainer("Development tools","All the stuff developers install on their laptop.","Windows");

        List<Container> buildStepContainers = Arrays.asList(
                continuousIntegrationSoftwareSystem.addContainer("Docker image builder", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("CPPCHECK version x", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("CPPCHECK version y", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("VS2017-builder", "build step", "docker, Windows"),
                continuousIntegrationSoftwareSystem.addContainer("VS2015-builder", "build step", "docker, Windows")
        );
        Container jenkinsDockerImageBuilderContainer = buildStepContainers.get(0);

        jenkinsBuildAgentContainer.addComponent(
                "Jenkins swarm client",
                "Client software that complements the Jenkins Self-Organizing Swarm Plug-in",
                "Java");
        jenkinsMasterContainer.addComponent(
                "Jenkins Self-Organizing Swarm Plug-in",
                "Plugin that makes the cloud of agents dynamic",
                "Jenkins plugin");
        jenkinsUnitTestAgentContainer.addComponent(
                "Jenkins swarm client",
                "Client software that complements the Jenkins Self-Organizing Swarm Plug-in",
                "Java");
        jenkinsHardwareTestAgentContainer.addComponent(
                "Jenkins swarm client",
                "Client software that complements the Jenkins Self-Organizing Swarm Plug-in",
                "Java");
        jenkinsIntegrationTestAgentContainer.addComponent(
                "Jenkins swarm client",
                "Client software that complements the Jenkins Self-Organizing Swarm Plug-in",
                "Java");
        codeRepositoryContainer.addComponent("svn", "Current SCM mechanism", "old");
        codeRepositoryContainer.addComponent("git", "Future SCM mechanism", "modern");

        // relationships
        developer.uses(continuousIntegrationSoftwareSystem,
                "Supplies input and starts builds.");
        developer.uses(continuousIntegrationSoftwareSystem,
                "Observes (and responds to) feedback from the build system.");
        releaseManager.uses(continuousIntegrationSoftwareSystem,
                "Manages public release of artifacts.");
        jenkinsBuildAgentContainer.uses(jenkinsUnitTestAgentContainer,
                "A build agent uses test agents to run (hardware dependent) tests, if specified in the Jenkinsfile.");
        jenkinsBuildAgentContainer.uses(jenkinsHardwareTestAgentContainer,
                "A build agent uses test agents to run (hardware dependent) tests, if specified in the Jenkinsfile.");
        jenkinsDockerImageBuilderContainer.uses(artifactRepositoryContainer,
                "Images for build steps are stored in the artifact repository for easy access and for posterity (so we can perform the same build in about 20 years from now).",
                "https",
                InteractionStyle.Synchronous);

        artifactRepositoryContainer.delivers(releaseManager,
                "The release manager receives the final product from the artifact repository",
                "https",
                InteractionStyle.Asynchronous);

        jenkinsAdmin.uses(artifactRepositoryContainer,
                "Store copies (i.e. 'cache') Docker images from external sources (e.g. Microsoft) to enable rebuilding build step images in about 20 years from now.");
        jenkinsAdmin.uses(dockerImageRepo,
                "Download docker images from external source for use in our internal build system.");
        jenkinsAdmin.uses(dockerImageRepoMS,
                "Download docker images from external source for use in our internal build system. NB: this may not be allowed (MS license) in which case the build step images must be stored for eternity.");

        // deployment model
        String sDtnInternalNetworkEnvironment = "DTN internal network";
        DeploymentNode jenkinsPrimeNode = model.addDeploymentNode(sDtnInternalNetworkEnvironment,
                "Jenkins Prime",
                "Beefy host machine for the Jenkins master, which has enough resources to also host at least one Jenkins build agent",
                "hardware, Windows Server 2019");
        DeploymentNode primeWindowsHost = jenkinsPrimeNode.addDeploymentNode(
                "Windows Server 2019",
                "Windows Server 2019",
                "Windows");
        DeploymentNode primeDockerHost = primeWindowsHost.addDeploymentNode(
                "Windows Docker host",
                "Docker host for Windows containers",
                "docker");
        primeWindowsHost.add(jenkinsMasterContainer).addTags(MyTags.PROCESS);
//        primeWindowsHost.add(jenkinsBuildAgentContainer);
        for (Container buildStepContainer: buildStepContainers) {
            primeDockerHost.add(buildStepContainer).addTags(MyTags.PROCESS);
        }

        DeploymentNode jenkinsHardwareTestMachine = model.addDeploymentNode(sDtnInternalNetworkEnvironment,
                "Jenkins hardware test machine","Host for hardware-dependent tests",
                "hardware");
        DeploymentNode jenkinsHardwareTestOS = jenkinsHardwareTestMachine.addDeploymentNode("Windows 10","Shouldn't this be the same OS that we're targeting in production?","Windows");
        jenkinsHardwareTestOS.add(jenkinsHardwareTestAgentContainer).addTags(MyTags.PROCESS, MyTags.JENKINS_EXECUTOR);

        DeploymentNode scmHost = model.addDeploymentNode(sDtnInternalNetworkEnvironment,"Repository Host","Host machine for source code repositories","hardware"); // redundancy?
        scmHost.add(codeRepositoryContainer).addTags(MyTags.REPOSITORY);
        scmHost.add(artifactRepositoryContainer).addTags(MyTags.REPOSITORY);

        // define some views (the diagrams you would like to see)
        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(continuousIntegrationSoftwareSystem, "SystemContext", "A System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        DeploymentView deploymentView = views.createDeploymentView("Deployment", "A Deployment diagram.");
        deploymentView.addAllDeploymentNodes();
   }

    private static void uploadWorkspaceToStructurizr(Workspace workspace) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);

        // "Workspace locking is not available with the Free Plan."
//        if (structurizrClient.lockWorkspace(WORKSPACE_ID)) {
//            structurizrClient.unlockWorkspace(WORKSPACE_ID);
//        }
//        else {
//            LogFactory.getLog(Structurizr.class).warn("The workspace isn't available right now!");
//        }

        // no need to archive the current state of the workspace
        structurizrClient.setWorkspaceArchiveLocation(null);
        // preserve existing diagram layout when uploading
        structurizrClient.setMergeFromRemote(true);
        // upload workspace to the cloud
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);

    }

}