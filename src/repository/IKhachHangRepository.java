package repository;

import Class.KhachHang;

public interface IKhachHangRepository extends IRepository<KhachHang> {
    KhachHang getByName(String ten);
}