param(
    [ValidateSet('jugar', 'entrenar', 'evaluar')]
    [string]$Accion = 'jugar',
    [int]$Partidas = 50000,
    [string]$Jdk = $env:JAVA_HOME
)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    if ($Jdk) {
        $compilador = Join-Path $Jdk 'bin/javac.exe'
        $java = Join-Path $Jdk 'bin/java.exe'
    } else {
        $compilador = (Get-Command javac -ErrorAction Stop).Source
        $java = (Get-Command java -ErrorAction Stop).Source
    }

    New-Item -ItemType Directory -Force 'target/classes' | Out-Null
    $fuentes = @(Get-ChildItem 'src/main/java' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })

    & $compilador -encoding UTF-8 -d target/classes @fuentes
    if ($LASTEXITCODE -ne 0) { throw 'Error de compilacion.' }

    if ($Accion -eq 'jugar') {
        & $java -cp target/classes totito.Principal
    } else {
        & $java -cp target/classes totito.Principal $Accion $Partidas
    }

    if ($LASTEXITCODE -ne 0) { throw 'La ejecucion termino con errores.' }
} finally {
    Pop-Location
}