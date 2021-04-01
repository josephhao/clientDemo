import java.util.Arrays;

public class Reflection {

    public static void main(String[] args) {
        EnumBuster<HumanState> buster =
                new EnumBuster<HumanState>(HumanState.class,
                        Human.class);
        HumanState ANGRY = buster.make("ANGRY");
        buster.addByValue(ANGRY);
        System.out.println(Arrays.toString(HumanState.values()));

        Human human = new Human();

        //
        human.sing(ANGRY);

    }


    enum HumanState {
        HAPPY, SAD
    }

    public static class Human {

        public void sing(HumanState state)
        {
            switch (state)
            {
                case HAPPY:
                    singHappySong();
                    break;
                case SAD:
                    singDirge();
                    break;
                default:
                    System.out.println(" default : ");
                    throw new IllegalStateException("Invalid State: " + state);
            }
        }

        private void singHappySong()
        {
            System.out.println("When you're happy and you know it ...");
        }

        private void singDirge()
        {
            System.out.println("Don't cry for me Argentina, ...");
        }
    }

    /**
     * 这个是Human 返编译后的代码；
     */
//    public static class Human {
//
//        public void sing(HumanState state) {
//            class Human$1 {
//                static final int[] $SwitchMap$com$hankcs$HumanState = new int[HumanState.values().length];
//
//                static {
//                    try {
//                        $SwitchMap$com$hankcs$HumanState[HumanState.HAPPY.ordinal()] = 1;
//                    } catch (NoSuchFieldError var2) {
//                        ;
//                    }
//
//                    try {
//                        $SwitchMap$com$hankcs$HumanState[HumanState.SAD.ordinal()] = 2;
//                    } catch (NoSuchFieldError var1) {
//                        ;
//                    }
//
//                }
//            }
//            switch(Human$1.$SwitchMap$com$hankcs$HumanState[state.ordinal()]) {
//                case 1:
//                    this.singHappySong();
//                    break;
//                case 2:
//                    this.singDirge();
//                    break;
//                default:
//                    new IllegalStateException("Invalid State: " + state);
//            }
//
//        }
//
//        private void singHappySong()
//        {
//            System.out.println("When you're happy and you know it ...");
//        }
//
//        private void singDirge()
//        {
//            System.out.println("Don't cry for me Argentina, ...");
//        }
//    }
}
