package repository;

import Class.NhanVien;

public interface INhanVienRepository extends IRepository<NhanVien> {
    NhanVien getByName(String ten);
}