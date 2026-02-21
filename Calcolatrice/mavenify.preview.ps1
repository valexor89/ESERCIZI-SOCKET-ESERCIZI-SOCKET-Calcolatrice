param(
  [string]$ProjectRoot = "."
)

$root = (Resolve-Path $ProjectRoot).Path
Write-Host "[preview] Root: $root"

$src = Join-Path $root "src"
$res = Join-Path $root "resources"
$mainJava = Join-Path $root "src\main\java"
$mainRes = Join-Path $root "src\main\resources"

$javaFiles = @()
if (Test-Path $src) {
  $javaFiles = Get-ChildItem -Path $src -Recurse -File -Filter *.java
}

Write-Host "[preview] Java files in src/: $($javaFiles.Count)"
if ($javaFiles.Count -gt 0) {
  $javaFiles | ForEach-Object { Write-Host "  - $($_.FullName.Substring($root.Length + 1))" }
}

if (Test-Path $res) {
  $resFiles = Get-ChildItem -Path $res -Recurse -File
  Write-Host "[preview] Resource files in resources/: $($resFiles.Count)"
  $resFiles | ForEach-Object { Write-Host "  - $($_.FullName.Substring($root.Length + 1))" }
} else {
  Write-Host "[preview] resources/ not found"
}

Write-Host "[preview] Planned target folders:"
Write-Host "  - src/main/java"
Write-Host "  - src/main/resources"

if (Test-Path (Join-Path $root "pom.xml")) {
  Write-Host "[preview] pom.xml already exists at project root"
} else {
  Write-Host "[preview] pom.xml will be created at project root"
}
