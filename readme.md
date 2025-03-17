# Swoopix App

Swoopix adalah aplikasi mobile berbasis Android yang menyediakan platform digital untuk layanan jasa. Aplikasi ini dibangun menggunakan Android Native dengan Java dan mengimplementasikan desain modern yang fokus pada pengalaman pengguna.

## 🛠️ Persyaratan Sistem
- Android Studio Hedgehog atau yang lebih baru
- JDK 17 atau yang lebih baru
- Android SDK minimal API 24 (Android 7.0)
- Gradle 8.2 atau yang lebih baru

## 📱 Fitur Utama
- Autentikasi pengguna (Login/Register)
- Dashboard interaktif
- Sistem notifikasi
- Manajemen profil
- Sistem pemesanan
- Dan fitur-fitur lainnya

## ⚙️ Cara Instalasi

### Untuk Pengguna
1. Download APK dari [Release Page](https://github.com/elprisdat/Swoopix/releases)
2. Izinkan instalasi dari sumber tidak dikenal di pengaturan Android Anda
3. Install APK dan jalankan aplikasi

### Untuk Developer
1. Clone repository
   ```bash
   git clone https://github.com/elprisdat/Swoopix.git
   cd Swoopix
   ```

2. Buka dengan Android Studio
   - Buka Android Studio
   - Pilih "Open an Existing Project"
   - Navigasi ke folder Swoopix
   - Klik "OK"

3. Setup Gradle
   - Tunggu proses sync Gradle selesai
   - Jika ada error, pastikan:
     - Android Studio sudah diupdate
     - SDK Tools sudah terinstall
     - Gradle version sesuai

4. Jalankan Aplikasi
   - Pilih device/emulator
   - Klik tombol "Run" (⟩) atau tekan Shift + F10

## 🎨 Panduan UI/UX

### Tema Warna
```xml
Primary: #2196F3
Secondary: #FFC107
Background: #FFFFFF
Text Primary: #212121
Text Secondary: #757575
```

### Tipografi
- Font Family: Roboto
- Ukuran Text:
  - Heading: 24sp
  - Subheading: 18sp
  - Body: 16sp
  - Caption: 14sp

### Layout Guidelines
- Padding standar: 16dp
- Margin antar elemen: 8dp
- Corner radius: 8dp
- Elevation: 2dp - 8dp

## Cara Berkontribusi ke Repository Swoopix

### 🚀 Langkah 1: Persiapan Awal
```bash
# Clone repository
git clone https://github.com/elprisdat/Swoopix.git

# Masuk ke direktori proyek
cd Swoopix
```

### 🌿 Langkah 2: Membuat Branch Baru
```bash
# Buat dan pindah ke branch baru
git checkout -b nama-fitur
```
Contoh nama branch yang baik:
- `feature/tambah-login`
- `fix/bug-navbar`
- `improvement/optimasi-loading`

### 💻 Langkah 3: Melakukan Perubahan
1. Buka code editor pilihan Anda
2. Lakukan perubahan yang diperlukan
3. Pastikan kode berjalan dengan baik
4. Test fitur yang diubah

### ✅ Langkah 4: Commit Perubahan
```bash
# Cek status perubahan
git status

# Tambahkan file yang akan di-commit
git add .

# Buat commit dengan pesan yang jelas
git commit -m "Deskripsi perubahan yang dilakukan"
```

Tips menulis pesan commit yang baik:
- "Menambahkan fitur login dengan Google"
- "Memperbaiki bug pada navigasi mobile"
- "Mengoptimasi performa loading halaman utama"

### ⬆️ Langkah 5: Push ke GitHub
```bash
git push origin nama-fitur
```

### 🔄 Langkah 6: Membuat Pull Request
1. Buka https://github.com/elprisdat/Swoopix
2. Klik tombol "Pull requests"
3. Klik "New pull request"
4. Pilih branch yang baru dibuat
5. Isi deskripsi pull request dengan:
   - Apa yang diubah
   - Mengapa perubahan diperlukan
   - Screenshot (jika ada perubahan visual)
6. Klik "Create pull request"

## 📝 Catatan Penting

### Update Repository Lokal
```bash
# Pindah ke branch master
git checkout master

# Ambil perubahan terbaru
git pull origin master
```

### Menangani Konflik
```bash
# Di branch kamu
git checkout nama-fitur
git fetch origin
git merge origin/master

# Selesaikan konflik jika ada
git add .
git commit -m "Merge master dan selesaikan konflik"
git push origin nama-fitur
```

### Panduan Tambahan
1. Gunakan pesan commit yang jelas dan dalam bahasa Indonesia
2. Satu fitur = Satu branch = Satu pull request
3. Jika ada perubahan yang diminta setelah review:
   ```bash
   # Lakukan perubahan
   git add .
   git commit -m "Memperbaiki feedback dari review"
   git push origin nama-fitur
   ```

## 🔐 Mendapatkan Akses Push

Jika Anda ingin mendapatkan akses push ke repository:
1. Hubungi owner repository untuk ditambahkan sebagai collaborator
2. Buat Personal Access Token:
   - Buka GitHub.com → Settings
   - Developer Settings → Personal Access Tokens → Tokens (classic)
   - Generate new token (classic)
   - Beri nama token
   - Centang scope "repo"
   - Generate dan simpan token dengan aman

## 📞 Kontak

Jika ada pertanyaan atau kendala, silakan hubungi:
- GitHub: [@elprisdat](https://github.com/elprisdat)
- Email: ariiq3st@gmail.com

## 📜 Lisensi

[Sesuaikan dengan lisensi yang digunakan]
