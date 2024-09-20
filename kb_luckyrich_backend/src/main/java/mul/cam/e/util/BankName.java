package mul.cam.e.util;

public enum BankName {
    KOOKMIN_BANK("국민은행"),
    KAKAO_BANK("카카오뱅크"),
    SHINHAN_BANK("신한은행");

    private final String name;

    BankName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
