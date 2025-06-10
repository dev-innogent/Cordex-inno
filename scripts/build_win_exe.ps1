# Build the project and create a Windows executable using jpackage
mvn clean package

# Find the built jar-with-dependencies
$jar = Get-ChildItem target/*jar-with-dependencies.jar | Select -First 1
if (-not $jar) {
    Write-Error "Jar not found."
    exit 1
}

jpackage `
  --type exe `
  --input target `
  --dest dist `
  --name DesktopAgent `
  --main-jar $($jar.Name) `
  --main-class com.desktop.agent.AgentApp `
  --win-console

