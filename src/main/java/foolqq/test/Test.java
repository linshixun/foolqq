package foolqq.test;

import foolqq.BaseQQWindowContext;
import foolqq.model.QQMsg;
import foolqq.tool.Serial;
import org.jnativehook.NativeHookException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {


    static boolean isOpen = true;
    static HashMap<String, String> faq;
    static String[] keyword = new String[]{"关闭服务", "打开服务"};

    public static void main(String[] args) throws AWTException, IOException, NativeHookException {

        faq = (HashMap<String, String>) Serial.loadHessian(new File("faq"));
        if (faq == null) {
            faq = new HashMap<>();
        }
        //创建BaseQQWindowContext的实例、传入之前做好的point.png文件

        HashMap<String, String> lastContent = new HashMap<>();

        BaseQQWindowContext context = new BaseQQWindowContext(new File("point.png")) {
            @Override
            public void onMessage(String name, QQMsg msg) {

                //name是图片名称(不包括扩展名),对前面提到的gj.png图片而言这里name就是gj，因此可以根据name判断到底是哪个群的消息

                System.out.println(msg);   //msg包括内容、发送人QQ、昵称、时间
                StringBuilder sb = new StringBuilder();
                if (!msg.getNick().equals("杭州-宝宝")) {
                    //所有功能忽略自己

                    if (msg.getContent().contains("宝宝")) {

                        //私有消息



                        openCloseService(msg, sb);


                        if (isOpen) {
                            if (msg.getContent().contains("刚刚说什么")) {
                                String learnRegx = "@([\\S\\s]+?)[\\s]*刚刚说什么";
                                Matcher matcher = Pattern.compile(learnRegx).matcher(msg.getContent());
                                if (matcher.find()) {
                                    if (lastContent.containsKey(matcher.group(1))) {
                                        sb.append(matcher.group(1) + "最后一条消息是:" + lastContent.get(matcher.group(1)));
                                    } else {
                                        sb.append("我也不知道呀");
                                    }
                                }
                            } else if (msg.getContent().contains("删除")) {
                                int size = faq.size();

                                Pattern compile = Pattern.compile("删除([\\S\\s]+?)$");
                                Matcher matcher = compile.matcher(msg.getContent().trim());
                                if (matcher.find()) {
                                    faq.remove(matcher.group(1));
                                    sb.append("已经删除问题:" + matcher.group(1));
                                }

                                if (size != faq.size()) {
                                    Serial.storeHessian(faq, "faq");
                                }
                            } else if (msg.getContent().contains("学") && msg.getContent().contains("答")) {
                                int size = faq.size();
                                String learnRegx = "[\\S\\s]*学[\\S\\s]+?答[\\S\\s]+";
                                String content = msg.getContent();
                                if (content.matches(learnRegx)) {
                                    Pattern compile = Pattern.compile("学([\\S\\s]+?)答([\\S\\s]+)");
                                    Matcher matcher = compile.matcher(msg.getContent().trim());
                                    if (matcher.find()) {


                                        String group = matcher.group(1).trim();
                                        String group1 = matcher.group(2).trim();

                                        for (String s : keyword) {
                                            group = group.replaceAll(s, "");
                                            group1 = group1.replaceAll(s, "");
                                        }
                                        faq.put(group, group1 + "\n由[" + msg.getNick() + "]提供");
                                        sb.append("我又学习到新技能咯: 问: " + group + " 答: " + group1 + "\n删除的口令是:宝宝 删除xxx");
                                    }
                                }

                                if (size != faq.size()) {
                                    Serial.storeHessian(faq, "faq");
                                }
                            } else {
                                try {
                                    String anser = Turing.getAnser(msg.getContent());

                                    if (anser != null)
                                        sb.append(anser);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                        } else {
                            sb.append("我已关闭\n请说: 宝宝 打开服务");
                        }


                        lastContent.put(msg.getNick(), msg.getContent());
                        if (sb.length() == "我是宝宝\n".length()) {
                            sb.append("我不知道说什么了,等我的主人回来教教我\n");
                            sb.append("学习的口令是:宝宝 学xxx答yyy\n");
                        }


                    } else {

                        //公共消息

                        if (isOpen) {
                            if (msg.getContent().contains("手续费") || msg.getContent().contains("百分百") || msg.getContent().contains("加微信") || msg.getContent().contains("包下款") || msg.getContent().contains("无视黑白") || msg.getContent().contains("http://") || msg.getContent().contains("https://")) {
                                sb.append("管理员 ," + msg.getNick() + "正在传播小广告\n");
                            } else if (msg.getContent().contains("红包")) {
                                sb.append("重大申明，本人拒收来自支付宝的任何形式的转账，要转账请直接转我qq钱包\n");
                            } else if (msg.getContent().contains("调戏")) {
                                sb.append("管理员 ," + msg.getNick() + "正在调戏妇女儿童\n");
                            } else {
                                faq.forEach((a, q) -> {
                                    if (msg.getContent().contains(a)) {
                                        sb.append(q);
                                    }
                                });

                                if (sb.length() == 0) {
                                    try {
                                        String anser = Turing.getAnser(msg.getContent());

                                        if (anser != null)
                                            sb.append(anser);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    if (sb.length() > 0) {
                        writeQQMsg(name, sb.append(" @" + msg.getNick() + "\n").toString());
                    }

                } else {
                    openCloseService(msg, sb);
                    if (msg.getContent().contains("刚刚说什么")) {
                        String learnRegx = "@([\\S\\s]+?)[\\s]*刚刚说什么";
                        Matcher matcher = Pattern.compile(learnRegx).matcher(msg.getContent());
                        if (matcher.find()) {
                            if (lastContent.containsKey(matcher.group(1))) {
                                sb.append(matcher.group(1) + "最后一条消息是:" + lastContent.get(matcher.group(1)));
                            } else {
                                sb.append("我也不知道呀");
                            }
                        }
                    }
                    if (sb.length() > 0) {
                        writeQQMsg(name, sb.toString());
                    }
                }


            }

        };
    }

    private static void openCloseService(QQMsg msg, StringBuilder sb) {
        if (msg.getContent().contains("关闭服务")) {
            isOpen = false;
            sb.append("已关闭\n");
        } else if (msg.getContent().contains("打开服务")) {
            isOpen = true;
            sb.append("已打开\n");
        }
    }
}
