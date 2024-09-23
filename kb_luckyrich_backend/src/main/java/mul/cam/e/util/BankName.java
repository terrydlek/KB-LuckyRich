package mul.cam.e.util;

import lombok.Getter;

@Getter
public enum BankName {
    국민은행("국민은행", 1),
    카카오뱅크("카카오뱅크", 2),
    신한은행("신한은행", 3);

    private final String name;
    private final int num;

    BankName(String name, int num) {
        this.name = name;
        this.num = num;
    }
}
