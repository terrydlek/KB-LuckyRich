package mul.cam.e.enumrate;

import lombok.Getter;

@Getter
public enum AccountType {
    청년도약("청년도약", 1),
    주택청약("주택청약", 2),
    자유적금("자유적금", 3),
    입출금통장("입출금통장", 4),
    비상금통장("비상금통장", 5);

    private final String type;
    private final int num;

    AccountType(String type, int num) {
        this.type = type;
        this.num = num;
    }
}
