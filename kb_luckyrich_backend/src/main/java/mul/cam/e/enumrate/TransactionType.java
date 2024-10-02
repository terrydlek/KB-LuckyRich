package mul.cam.e.enumrate;

public enum TransactionType {
    입금(new String[]{"급여", "아르바이트", "용돈", "금융수입", "기타"}),
    출금(new String[]{"식비", "카페", "술", "쇼핑", "교통", "여가", "여행", "경조"});

    private final String[] categories;

    TransactionType(String[] categories) {
        this.categories = categories;
    }

    public String[] getCategories() {
        return categories;
    }
}