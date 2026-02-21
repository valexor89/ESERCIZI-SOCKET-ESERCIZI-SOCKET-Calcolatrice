param(
  [string]$ProjectRoot = ".",
  [string]$GroupId = "com.tpsit",
  [string]$ArtifactId = "calcolatrice",
  [string]$Version = "1.0.0-SNAPSHOT",
  [string]$JavaVersion = "17",
  [switch]$SkipBackup
)

$ErrorActionPreference = "Stop"
$root = (Resolve-Path $ProjectRoot).Path
Write-Host "[convert] Root: $root"

$src = Join-Path $root "src"
$res = Join-Path $root "resources"
$main = Join-Path $root "src\main"
$mainJava = Join-Path $main "java"
$mainRes = Join-Path $main "resources"

if (-not (Test-Path $src)) {
  throw "Missing folder: src"
}

if (-not $SkipBackup) {
  $stamp = Get-Date -Format "yyyyMMdd-HHmmss"
  $backup = Join-Path $root ("_backup-pre-maven-" + $stamp)
  New-Item -ItemType Directory -Path $backup | Out-Null

  foreach ($name in @("src", "resources", "lib", "bin")) {
    $p = Join-Path $root $name
    if (Test-Path $p) {
      Copy-Item -Path $p -Destination $backup -Recurse -Force
      Write-Host "[backup] copied $name -> $backup"
    }
  }

  $pom = Join-Path $root "pom.xml"
  if (Test-Path $pom) {
    Copy-Item -Path $pom -Destination $backup -Force
    Write-Host "[backup] copied pom.xml -> $backup"
  }
}

New-Item -ItemType Directory -Path $mainJava -Force | Out-Null
New-Item -ItemType Directory -Path $mainRes -Force | Out-Null

# Move java files from src/ to src/main/java, excluding existing Maven tree.
$javaFiles = Get-ChildItem -Path $src -Recurse -File -Filter *.java |
  Where-Object { $_.FullName -notlike "*\src\main\*" -and $_.FullName -notlike "*\src\test\*" }

foreach ($file in $javaFiles) {
  $rel = $file.FullName.Substring($src.Length).TrimStart('\\')
  $dest = Join-Path $mainJava $rel
  $destDir = Split-Path $dest -Parent
  New-Item -ItemType Directory -Path $destDir -Force | Out-Null
  Move-Item -Path $file.FullName -Destination $dest -Force
  Write-Host "[move] $($file.FullName.Substring($root.Length + 1)) -> $($dest.Substring($root.Length + 1))"
}

# Move resources/ content to src/main/resources
if (Test-Path $res) {
  $resFiles = Get-ChildItem -Path $res -Recurse -File
  foreach ($file in $resFiles) {
    $rel = $file.FullName.Substring($res.Length).TrimStart('\\')
    $dest = Join-Path $mainRes $rel
    $destDir = Split-Path $dest -Parent
    New-Item -ItemType Directory -Path $destDir -Force | Out-Null
    Move-Item -Path $file.FullName -Destination $dest -Force
    Write-Host "[move] $($file.FullName.Substring($root.Length + 1)) -> $($dest.Substring($root.Length + 1))"
  }
}

# Clean empty legacy directories.
if (Test-Path $res) {
  Remove-Item -Path $res -Recurse -Force
  Write-Host "[cleanup] removed resources/"
}

# Remove empty folders left in src except main/test
$dirs = Get-ChildItem -Path $src -Recurse -Directory |
  Sort-Object FullName -Descending |
  Where-Object { $_.FullName -notlike "*\src\main*" -and $_.FullName -notlike "*\src\test*" }

foreach ($d in $dirs) {
  if ((Get-ChildItem -Path $d.FullName -Force | Measure-Object).Count -eq 0) {
    Remove-Item -Path $d.FullName -Force
  }
}

$pomPath = Join-Path $root "pom.xml"
$pom = @"
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>$GroupId</groupId>
  <artifactId>$ArtifactId</artifactId>
  <version>$Version</version>

  <properties>
    <maven.compiler.source>$JavaVersion</maven.compiler.source>
    <maven.compiler.target>$JavaVersion</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>
      <version>2.23.1</version>
    </dependency>
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>2.23.1</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>3.2.0</version>
      </plugin>
    </plugins>
  </build>
</project>
"@

Set-Content -Path $pomPath -Value $pom -Encoding UTF8
Write-Host "[write] pom.xml created/updated"

Write-Host "[done] Project converted to Maven layout"
Write-Host "[next] mvn clean compile"
Write-Host "[next] mvn -Dexec.mainClass=ServerApp exec:java"
Write-Host "[next] mvn -Dexec.mainClass=ClientApp exec:java"
