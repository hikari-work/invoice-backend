<p align="center">
<img src="res/qriss.png" alt="QR Code" width="150"/>
</p>

# QRIS OrderKuota Payment Verifikator

Sebuah api yang bisa mendapatkan status pembayaran melalui
QRIS OrderKuota.

## Fitur
- Melihat mutasi QRIS OrderKuota
- Sudah

Dukung saya di, dimana

Instruksi Pemakaian

1. Install Docker
   From `https://get.docker.com`:
```shell
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
```
2. Clone  Repository
3. Jalankan Install Dependecy maven
   ```shell
   mvn clean package
   ```
4. Edit Flag in Dockerfile
5. Jalankan
```shell
docker build -t orkut-integration-app .
```
6. Jalankan
```shell
docker run -d --name integration-orderkuota -p 9999:9999 orkut-integration-app
```
