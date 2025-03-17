# Swoopix App

Aplikasi Swoopix adalah platform digital yang memungkinkan pengguna untuk [deskripsi singkat aplikasi].

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
