import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Timer; //计时器

//第一种敌机
class Airplane extends FlyingObject {
    private int speed = 3;

    public Airplane() {
        this.image = ShootGame.airplane;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random rand = new Random();
        x = rand.nextInt(ShootGame.WIDTH - width); //
    }

    //击中时返回分数
    public int getScore() {
        return 3;
    }

    //越界判断
    public boolean outOfBounds() {
        return y > ShootGame.HEIGHT;
    }

    public void step() {
        y += speed;
    }//移动
}

//第二种敌机
class airplane1 extends FlyingObject {
    private int xSpeed = 1;    //x坐标移动速度
    private int ySpeed = 2;    //y坐标移动速度

    public airplane1() {
        this.image = ShootGame.airplane1;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random rand = new Random();
        x = rand.nextInt(ShootGame.WIDTH - width);//在随机位置生成
    }

    public boolean outOfBounds() {
        return y > ShootGame.HEIGHT;
    }//越界判断

    //移动
    public void step() {
        x += xSpeed;
        y += ySpeed;
        if (x > (ShootGame.WIDTH - width)) {
            xSpeed = -1;
        }
        if (x < 0) {
            xSpeed = 1;
        }
    }

    //击中返回分数
    public int getScore() {
        return 5;
    }
}

//子弹
class Bullet extends FlyingObject {
    private int speed = 7;

    public Bullet(int x, int y) {  //x,y为子弹横纵坐标
        this.x = x;
        this.y = y;
        this.image = ShootGame.bullet;
    }

    public void step() {
        y -= speed;
    }

    public boolean outOfBounds() {
        return y < 0;
    }

}

//生命药水
class Award extends FlyingObject {
    private int xSpeed = 1;    //x坐标移动速度
    private int ySpeed = 1;    //y坐标移动速度

    public Award() {
        this.image = ShootGame.life;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random rand = new Random();
        x = rand.nextInt(ShootGame.WIDTH - width);//面板宽-bee宽
    }

    public boolean outOfBounds() {
        return y > ShootGame.HEIGHT;
    }//越界

    public void step() {
        x += xSpeed;
        y += ySpeed;
        if (x > (ShootGame.WIDTH - width)) {
            xSpeed = -1;
        }
        if (x < 0) {
            xSpeed = 1;
        }
    }//移动

}

abstract class FlyingObject {//所有飞行物
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage image;    //图片

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public BufferedImage getImage() {
        return image;
    }

    public abstract boolean outOfBounds();

    public abstract void step();//移动

    public boolean shootBy(Bullet bullet) {
        int x = bullet.x;
        int y = bullet.y;
        return (this.x < x) && (x < (this.x + width)) && this.y < y && y < (this.y + height);
    }//是否被击中

}

//英雄机
class Hero extends FlyingObject {
    private int life;
    public int xspeed = 10;
    public int yspeed = 10;

    public Hero() {
        life = 3;
        image = ShootGame.hero0;
        width = image.getWidth();
        height = image.getHeight();
        x = 150;
        y = 400;//飞机初始位置
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void hurt() {
        life--;
    }//受伤

    public void recover() {
        life++;
    }//回血

    public int getLife() {
        return life;
    }//获取剩余生命

    //根据鼠标移动
    public void moveTo(int x, int y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }

    //根据键盘操作移动
    public void moveTo1(int x, int y) {
        int t1, t2;
        t1 = this.x;
        t2 = this.y;
        this.x = x;
        this.y = y;
        if (outOfBounds()) {
            this.x = t1;
            this.y = t2;
        }
    }

    public boolean outOfBounds() {
        return (x < (-width / 2)) || (x > (ShootGame.WIDTH - width / 2)) || (y < (-height / 2)) || (y > (ShootGame.HEIGHT - height));
    }

    //子弹发射
    public Bullet[] shoot() {
        int xb = width / 2 - 3;
        int yb = height / 2 - 28;//子弹相对飞机形成位置
        Bullet[] bullets = new Bullet[1]; //形成子弹对象
        bullets[0] = new Bullet(x + xb, y - yb);
        return bullets;
    }

    //实现抽象方法
    public void step() {
    }

    public boolean hit(FlyingObject other) {
        int x1 = other.x - this.width / 2;
        int x2 = other.x + this.width / 2 + other.width;
        int y1 = other.y - this.height / 2;
        int y2 = other.y + this.height / 2 + other.height;
        int herox = this.x + this.width / 2;
        int heroy = this.y + this.height / 2;
        return herox > x1 && herox < x2 && heroy > y1 && heroy < y2;
    }//英雄机中心点碰撞判断
}

//根据键盘移动
class HeroMove {
    public void move(Hero hero, boolean w, boolean s, boolean a, boolean d) {
        //hero为英雄机对象，w/s/a/d分别为键盘上w/s/a/d是否被按下的布尔值
        int x, y;
        x = hero.getX();
        y = hero.getY();
        if (w) {
            if (!hero.outOfBounds()) {//不越界则移动
                hero.moveTo1(x, y - hero.yspeed);
            }
        }
        if (s) {
            if (!hero.outOfBounds()) {
                hero.moveTo1(hero.getX(), hero.getY() + hero.yspeed);
            }
        }
        if (a) {
            if (!hero.outOfBounds()) {
                hero.moveTo1(hero.getX() - hero.xspeed, hero.getY());
            }
        }
        if (d) {
            if (!hero.outOfBounds()) {
                hero.moveTo1(hero.getX() + hero.xspeed, hero.getY());
            }
        }
    }
}

//更新排行榜并排序
class Write {
    public void wri(int score) { //score为游戏结束时返回的分数
        int[] a = new int[15];//存放成绩
        File file = new File("D:\\score.txt");
        if (!file.exists()) {//判断文件是否存在
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {//将排行榜更新
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            int i = 0;
            while ((s = in.readLine()) != null) {
                if (i >= 10) {
                    break;
                }
                a[i++] = Integer.parseInt(s);
            }
            a[i++] = score;  //将新成绩加入
            int len = i;
            for (int k = len - 1; k > 0; k--) {
                for (int t = 0; t < k; t++) {
                    if (a[t] < a[t + 1]) {
                        int g = a[t];
                        a[t] = a[t + 1];
                        a[t + 1] = g;
                    }
                }
            }
            BufferedWriter out1 = new BufferedWriter(new FileWriter(file));//替换
            out1.write("");
            out1.flush();
            out1.close();   //先清空在将新数据写入
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); //追加
            for (int t = 0; t < len && t < 10; t++) {
                out.write(String.valueOf(a[t]));
                out.newLine();
            }
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ShootGame extends JPanel {  //面板组件
    public static final int WIDTH = 400;
    public static final int HEIGHT = 700;//窗口的宽、长
    public static int rank = 0;  //是否输出排名
    public static int rank1 = 0; //锁定排名界面
    public static int skill = 0; //技能发动指示器
    public static int skilltime = 0;//技能持续时间（发动技能时改变）
    public static int skilluse = 2;//技能可发动次数
    public static int restart = 1;//是否重新开始
    private static int state = 0;//0准备,1开始，2暂停，3结束

    private int score = 0;        // 得分
    private Timer timer;          // 定时器
    private int intervel = 10;    // 刷新时间间隔(毫秒)

    FlyingObject[] flyings = {};  // 敌机数组
    Bullet[] bullets = {};   // 子弹数组
    public Hero hero = new Hero();  //英雄机

    public static BufferedImage point;
    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage airplane;
    public static BufferedImage airplane1;
    public static BufferedImage bullet;
    public static BufferedImage hero0;
    public static BufferedImage pause;
    public static BufferedImage gameover;
    public static BufferedImage life;//静态初始化图片资源

    static {
        try {
                point = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("score.png")));
            background = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("world.png")));
            start = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("start.png")));
            airplane = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("airplane.png")));
            airplane1 = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("airplane1.png")));
            bullet = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("bullet.png")));
            hero0 = ImageIO.read(Objects.<URL>requireNonNull(ShootGame.class.getResource("hero0.png")));
            pause = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("pause.png")));
            gameover = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("gameover.png")));
            life = ImageIO.read(Objects.requireNonNull(ShootGame.class.getResource("life.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {  //输出画面，g为绘画对象
        if (rank == 1) {
            paintRank(g);
        } else {
            g.drawImage(background, 0, 0, null);  // 画背景图
            paintHero(g);
            paintBullets(g);
            paintFlyingObjects(g);
            if (state != 0) {
                paintScore(g);
            }
            paintState(g);
        }
    }

    //画英雄机
    public void paintHero(Graphics g) {
        g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
    }

    //画子弹
    public void paintBullets(Graphics g) {
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            g.drawImage(b.getImage(), b.getX() - b.getWidth() / 2, b.getY(), null);
        }
    }

    //画敌机
    public void paintFlyingObjects(Graphics g) {
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            g.drawImage(f.getImage(), f.getX(), f.getY(), null);
        }
    }

    //画排名
    public void paintRank(Graphics g) {
        if (rank == 1 && rank1 == 0) {
            g.drawImage(point, 0, 0, null);
            File file = new File("D:\\score.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                String s;
                int x = 120;
                int y = 35;
                int i = 0;
                try {
                    while ((s = in.readLine()) != null) {
                        if (i >= 10) {
                            break;
                        }
                        int t = Integer.parseInt(s);
                        if (i == 0) {
                            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 35);// 字体
                            g.setColor(new Color(10, 10, 11));
                            g.setFont(font);  //设置字体
                            g.drawString("历史排名", 130, y);  //分数
                            y = y + 60;
                        }
                        i++;
                        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 22);// 字体
                        g.setColor(new Color(10, 10, 11));
                        g.setFont(font);  //设置字体
                        g.drawString("第" + i + "名" + "：" + t, x, y);  //分数
                        y = y + 50;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        rank1 = 1;//停止打印排名
    }

    //画分数、生命、技能、提示
    public void paintScore(Graphics g) {
        int x = 10;
        int y = 25;
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 22);
        Font font1 = new Font(Font.SANS_SERIF, Font.BOLD, 10);  // 字体
        g.setColor(new Color(245, 245, 245));
        g.setFont(font);  //设置字体
        g.drawString("得分:" + score, x, y);  //分数
        y = y + 20;
        g.drawString("生命值:" + hero.getLife(), x, y);  //生命
        y = y + 20;
        g.setFont(font1);  //设置字体
        g.drawString("按“Q”发动技能：世界(时间暂停)", x, y);
    }

    //画状态
    public void paintState(Graphics g) {
        switch (state) {
            case 0:  // 启动
                g.drawImage(start, 0, 0, null);
                int x = 10;
                int y = 25;
                Font font = new Font(Font.SANS_SERIF, Font.TYPE1_FONT, 15);// 字体
                g.setColor(new Color(237, 230, 219));
                g.setFont(font);  //设置字体
                g.drawString("按“空格”或鼠标点击开始游戏", x, y);
                y += 40;
                g.drawString("按”R“查看排行榜", x, y);
                break;
            case 2:  // 暂停
                g.drawImage(pause, 0, 0, null);
                break;
            case 3:  // 游戏结束
                g.drawImage(gameover, 0, 0, null);
                break;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("飞机大战");//窗体
        ShootGame game = new ShootGame();  // 面板对象
        frame.add(game);  // 将面板对象添加到JFrame中
        frame.setSize(WIDTH, HEIGHT);  // 设置大小
        frame.setAlwaysOnTop(true);  //设置其总在最上
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 默认关闭操作
        frame.setLocationRelativeTo(null);  // 设置窗体初始位置
        frame.setVisible(true);  // 允许并调用paint
        game.action(frame);  // 启动执行，将面板对象传递给键盘监听函数
    }

    public static boolean gow = false, gos = false, goa = false, god = false;//判断键盘的w/s/a/d按键是否处于按下的状态

    public void action(JFrame frame) {//启动
        //为窗体设置键盘监听
        frame.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {//按键释放监听
                char A = e.getKeyChar();
                switch (A) {
                    case 'w':
                        gow = false;
                        break;
                    case 's':
                        gos = false;
                        break;
                    case 'a':
                        goa = false;
                        break;
                    case 'd':
                        god = false;
                        break;
                }
            }

            public void keyPressed(KeyEvent e) {//按键按压监听
                char A = e.getKeyChar();
                switch (A) {
                    case ' ':   //空格与鼠标左键功能相同
                        switch (state) {
                            case 0:
                                if (rank == 1) {
                                    rank = 0;//退出排行榜
                                } else {
                                    state = 1;  // 启动状态下运行
                                }
                                break;
                            case 1:
                                state = 2;
                                restart = 0;
                                break;
                            case 2:
                                state = 1;
                                break;
                            case 3:  // 游戏结束，清理现场
                                flyings = new FlyingObject[0];  // 清空飞行物
                                bullets = new Bullet[0];  // 清空子弹
                                hero = new Hero();  // 重新创建英雄机
                                score = 0;  // 清空成绩
                                state = 0;  // 状态设置为开始
                                break;
                        }
                        break;
                    case 'q':
                        if (skilluse > 0) {
                            skill = 1;
                        }
                        break;
                    case 'r':
                        if (state == 0) {
                            rank = 1;
                            rank1 = 0;//允许打印排名
                        }
                        break;
                    case 'w':
                        if (state == 1) {
                            gow = true;
                        }
                        break;
                    case 's':
                        if (state == 1) {
                            gos = true;
                        }
                        break;
                    case 'a':
                        if (state == 1) {
                            goa = true;
                        }
                        break;
                    case 'd':
                        if (state == 1) {
                            god = true;
                        }
                        break;
                }
            }
        });

        // 鼠标监听事件
        MouseAdapter l = new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {  // 鼠标移动
                if (state == 1) {  // 运行状态下移动英雄机
                    int x = e.getX();//读取鼠标位置
                    int y = e.getY();
                    hero.moveTo(x, y);
                }
            }

            public void mouseEntered(MouseEvent e) {  // 鼠标进入
                if (state == 2) {  // 暂停状态下运行
                    state = 1;
                }
            }

            public void mouseClicked(MouseEvent e) {  // 鼠标点击
                switch (state) {
                    case 0:
                        if (rank == 1) {
                            rank = 0;//退出排行榜
                        } else {
                            state = 1;  // 启动状态下运行
                        }
                        break;
                    case 1:
                        state = 2;
                        restart = 0;
                        break;
                    case 2:
                        state = 1;
                        break;
                    case 3:  // 游戏结束，清理现场
                        flyings = new FlyingObject[0];  // 清空飞行物
                        bullets = new Bullet[0];  // 清空子弹
                        hero = new Hero();  //重置英雄机
                        score = 0;  // 清空成绩
                        state = 0;  // 状态设置为开始
                        break;
                }
            }
        };
        this.addMouseListener(l);         // 处理鼠标点击操作
        this.addMouseMotionListener(l);  // 处理鼠标滑动操作
        //定时任务
        timer = new Timer();  // 主流程控制
        TimerTask task = new TimerTask() {
            public void run() {
                if (state == 0) {
                    skilluse = 2;//每次死亡更新技能次数
                } else if (state == 1 && skill == 0) {  // 运行状态
                    enterAction();  // 飞行物入场
                    stepAction();  // 走一步
                    shootAction();  // 英雄机射击
                    bangAction();  // 子弹打飞行物
                    outOfBoundsAction();  // 删除越界飞行物及子弹
                    GameOvercheck();  // 检查游戏结束
                } else if (state == 1 && skill == 1) {
                    if (skilltime > 200) {
                        //技能持续时间结束
                        skill = 0;
                        skilluse--;//技能次数减一
                        skilltime = 0;//重置结束时间
                    }
                    skilltime++;
                    stepAction();  // 移动
                    shootAction();  // 英雄机射击
                    bangAction();  // 子弹打飞行物
                    outOfBoundsAction();  // 删除越界飞行物及子弹
                    GameOvercheck();  // 检查游戏结束
                }
                HeroMove hm = new HeroMove();
                hm.move(hero, gow, gos, goa, god);//hero为英雄机对象，w/s/a/d分别为键盘上w/s/a/d是否被按下的布尔值
                repaint();  // 重新绘制
            }

        };
        timer.schedule(task, intervel, intervel);//十毫秒后执行任务，并每隔十毫秒执行一次
    }

    int flycreate = 0;  // 敌机入场计数

    public void enterAction() {//飞行物入场
        flycreate++;
        if (flycreate % 40 == 0) {  // 400毫秒生成一个飞行物
            FlyingObject obj = New();  // 随机生成一个飞行物——一号敌机、二号敌机、血包
            flyings = Arrays.copyOf(flyings, flyings.length + 1);//长度增加
            flyings[flyings.length - 1] = obj;
        }
    }

    //移动
    public void stepAction() {
        if (skill == 0 || skilluse == 0) {//判断技能发动
            for (int i = 0; i < flyings.length; i++) {  // 敌机移动
                FlyingObject f = flyings[i];
                f.step();
            }
        }

        for (int i = 0; i < bullets.length; i++) {  // 子弹移动
            Bullet b = bullets[i];
            b.step();
        }
        hero.step();  // 英雄机移动
    }

    int shootIndex = 0;  // 射击计数

    //射击
    public void shootAction() {
        shootIndex++;
        if (shootIndex % 30 == 0) {  // 300毫秒发一颗
            shootIndex = 0;
            Bullet[] bs = hero.shoot();  // 英雄机打出子弹
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length);  // 扩容
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); //追加
        }
    }

    //子弹与飞行物碰撞检测
    public void bangAction() {
        for (int i = 0; i < bullets.length; i++) {  // 遍历所有子弹
            Bullet b = bullets[i];
            bang(b);  // 子弹和飞行物之间的碰撞检查
        }
    }

    //删除越界飞行物及子弹
    public void outOfBoundsAction() {
        int index = 0;
        FlyingObject[] flyingLives = new FlyingObject[flyings.length];  // 存放正常飞行物
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            if (!f.outOfBounds()) {
                flyingLives[index++] = f;  // 不越界的留着
            }
        }
        flyings = Arrays.copyOf(flyingLives, index);  //更新飞行物

        index = 0;  // 重置
        Bullet[] bulletLives = new Bullet[bullets.length];//存放未越界子弹
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            if (!b.outOfBounds()) {
                bulletLives[index++] = b;
            }
        }
        bullets = Arrays.copyOf(bulletLives, index);  //更新子弹
    }

    //游戏结束时执行相关操作
    public void GameOvercheck() {
        if (isGameOver() == true) {
            state = 3;
            Write g = new Write();
            g.wri(score);
        }
    }

    //游戏结束判断
    public boolean isGameOver() {
        for (int i = 0; i < flyings.length; i++) {
            int index = -1;
            FlyingObject obj = flyings[i];
            if (hero.hit(obj)) {  // 检查英雄机与飞行物是否碰撞
                if (obj instanceof Award) {
                    hero.recover();//回复
                } else {
                    hero.hurt();  // 减命
                }
                index = i;  // 记录碰上的飞行物索引
            }
            if (index != -1) {
                FlyingObject t = flyings[index];
                flyings[index] = flyings[flyings.length - 1];
                flyings[flyings.length - 1] = t;
                flyings = Arrays.copyOf(flyings, flyings.length - 1);
                //将被撞物体移动到数组最后，再用copyof删除
            }
        }
        return hero.getLife() <= 0;
    }

    public void bang(Bullet bullet) {//子弹和飞行物之间的碰撞检查
        int index = -1;  // 击中的飞行物索引
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject obj = flyings[i];
            if (obj.shootBy(bullet)) {  // 判断是否击中
                index = i;  // 记录被击中的飞行物
                break;
            }
        }
        if (index != -1) {  // 有击中的飞行物
            FlyingObject one = flyings[index];  // 记录被击中的飞行物
            //非奖励类可击中
            if (!(one instanceof Award)) {
                FlyingObject temp = flyings[index];
                flyings[index] = flyings[flyings.length - 1];
                flyings[flyings.length - 1] = temp;

                flyings = Arrays.copyOf(flyings, flyings.length - 1);  // 删除最后一个飞行物(即被击中的)
            }
            if (one instanceof Airplane) {  // 检查类型，是敌人，则加分
                Airplane e = (Airplane) one;
                score += e.getScore();  // 根据击中目标加分
            } else if (one instanceof airplane1) {
                airplane1 e = (airplane1) one;
                score += e.getScore();  // 根据击中目标加分
            }
        }
    }

    public static FlyingObject New() {//随机生成飞行物
        Random random = new Random();
        int type = random.nextInt(100);  // [0,99]
        if (type < 20) {
            return new airplane1();
        } else if (type > 97) {
            return new Award();
        } else {
            return new Airplane();
        }
    }
}
