# Script para criar veiculos e viagens
Write-Host "`n=== INICIANDO CADASTRO ===" -ForegroundColor Cyan

# 1. Buscar motoristas
Write-Host "`n1. Buscando motoristas..." -ForegroundColor Yellow
$drivers = (Invoke-RestMethod -Uri "http://localhost:8080/api/drivers?size=100" -Method Get).content
Write-Host "   Motoristas encontrados: $($drivers.Count)" -ForegroundColor Green

# 2. Criar veiculos
Write-Host "`n2. Criando veiculos..." -ForegroundColor Yellow
$vehiclesList = @(
    @('ABC-1A34','CAR','Toyota','Corolla',2020,45000),
    @('DEF-5B78','CAR','Honda','Civic',2021,32000),
    @('GHI-9C12','TRUCK','Ford','F-150',2019,78000),
    @('JKL-3D56','VAN','Mercedes','Sprinter',2022,15000),
    @('MNO-7E90','CAR','VW','Golf',2021,28000),
    @('PQR-1F22','CAR','Chevrolet','Onix',2020,55000),
    @('STU-3G44','TRUCK','Volvo','FH16',2018,120000),
    @('VWX-5H66','BUS','Mercedes','OF-1721',2019,95000),
    @('YZA-7I88','CAR','Hyundai','HB20',2022,18000),
    @('BCD-9J00','VAN','Fiat','Ducato',2021,42000),
    @('EFG-2K11','CAR','Nissan','Kicks',2023,12000),
    @('HIJ-4L33','TRUCK','Scania','R450',2020,85000),
    @('KLM-6M55','CAR','Jeep','Compass',2021,38000),
    @('NOP-8N77','BUS','Marcopolo','Paradiso',2019,110000),
    @('QRS-1O00','VAN','Renault','Master',2020,62000),
    @('TUV-3P22','CAR','Peugeot','208',2022,25000),
    @('WXY-5Q44','TRUCK','Iveco','Daily',2021,48000),
    @('ZAB-7R66','CAR','Ford','Ka',2023,8000),
    @('CDE-9S88','CAR','Fiat','Argo',2022,15000),
    @('FGH-1T57','CAR','Renault','Kwid',2021,22000)
)

$vehicles = @()
foreach ($vData in $vehiclesList) {
    $body = @{
        licensePlate = $vData[0]
        type = $vData[1]
        brand = $vData[2]
        model = $vData[3]
        year = $vData[4]
        status = 'AVAILABLE'
        currentMileage = $vData[5]
    } | ConvertTo-Json
    
    try {
        $v = Invoke-RestMethod -Uri "http://localhost:8080/api/vehicles" -Method Post -Body $body -ContentType "application/json" -ErrorAction Stop
        $vehicles += $v
        Write-Host "   OK $($vData[0])" -ForegroundColor Gray
    } catch {}
}
Write-Host "   Veiculos criados: $($vehicles.Count)" -ForegroundColor Green

# 3. Criar 100 viagens
Write-Host "`n3. Criando 100 viagens..." -ForegroundColor Yellow

$cities = @('Sao Paulo','Rio de Janeiro','Belo Horizonte','Brasilia','Curitiba','Porto Alegre','Salvador','Recife','Fortaleza','Manaus')
$created = 0

for ($i = 0; $i -lt 100; $i++) {
    $driver = $drivers[$i % $drivers.Count]
    $vehicle = $vehicles[$i % $vehicles.Count]
    $origin = $cities[(Get-Random -Minimum 0 -Maximum $cities.Count)]
    $destination = $cities[(Get-Random -Minimum 0 -Maximum $cities.Count)]
    while ($destination -eq $origin) {
        $destination = $cities[(Get-Random -Minimum 0 -Maximum $cities.Count)]
    }
    
    $daysAgo = Get-Random -Minimum 1 -Maximum 90
    $startDate = (Get-Date).AddDays(-$daysAgo).ToString('yyyy-MM-ddTHH:mm:ss')
    $distance = Get-Random -Minimum 50 -Maximum 500
    $hours = [Math]::Round($distance / 80, 1)
    $endDate = (Get-Date).AddDays(-$daysAgo).AddHours($hours).ToString('yyyy-MM-ddTHH:mm:ss')
    
    $body = @{
        vehicleId = $vehicle.id
        driverId = $driver.id
        origin = $origin
        destination = $destination
        startDateTime = $startDate
        endDateTime = $endDate
        startMileage = $vehicle.currentMileage
        endMileage = $vehicle.currentMileage + $distance
        distanceTraveled = $distance
        notes = "Viagem automatica"
    } | ConvertTo-Json
    
    try {
        Invoke-RestMethod -Uri "http://localhost:8080/api/trips" -Method Post -Body $body -ContentType "application/json" -ErrorAction Stop | Out-Null
        $created++
        if ($created % 10 -eq 0) {
            Write-Host "   Criadas: $created" -ForegroundColor Gray
        }
    } catch {
        Write-Host "   Erro na viagem $($i+1)" -ForegroundColor Red
    }
}

Write-Host "`n=== CONCLUIDO ===" -ForegroundColor Green
Write-Host "Motoristas: $($drivers.Count)" -ForegroundColor Cyan
Write-Host "Veiculos: $($vehicles.Count)" -ForegroundColor Cyan
Write-Host "Viagens criadas: $created" -ForegroundColor Cyan

