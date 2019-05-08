package Principal;

class M {

    public static void main(String[] args) {
        M ex = new M();
    }

    private M() {
        System.out.println(System.getProperty("user.dir"));
    }
}