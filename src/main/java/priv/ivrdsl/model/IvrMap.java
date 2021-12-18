package priv.ivrdsl.model;

import java.util.*;

/**
 * IVR 树状关系数据结构.
 * <p>
 * IVR 的业务逻辑呈现为树状，具体表现为以初始状态“0”为树的根节点，每一个 {@code event} 都是“0”的子节点。如果一个 {@code event} 的 {@code action} 是 {@link EnumBean.Action#ACTION_MENU}，则意味着在该事件节点下存在另一组子节点。它们组成了 IVR 业务的一个次级菜单，在客户按下 {@code ACTION_MENU} 对应的按键后进入。而如果 {@code action} 不是 {@code ACTION_MENU} ，那么这个 {@code event} 就已经成为了叶子结点。
 * <p>
 * 对于这个逻辑树，我们令根节点的绝对路径为“0”。用户通过按键到达一个事件节点，则这个事件节点相对根节点的路径就是用户按键的顺序。因此可以将每一个事件节点的绝对路径表示为 {@code "0" + 用户到达该节点的按键顺序}。
 * <p>
 * {@code IvrMap} 中有一个 Map{@literal <}String, List{@literal <}String{@literal >>} 类型的 {@code HashMap}
 * ，用以记录每一个事件节点绝对路径和事件信息（包括事件 {@code event}，动作 {@code action} 和补充信息 {@code additions}）的映射关系（trigger-event映射），以此表示 IVR
 * 脚本的业务逻辑。
 *
 * @author Guanidine Beryllium
 */
public class IvrMap implements Comparator<Map.Entry<String, List<String>>> {
    private static volatile IvrMap schema;

    private IvrMap() {
        map = new HashMap<>();
    }

    private final Map<String, List<String>> map;

    private static final HashMap<Character, Integer> ORDER_MAP = new HashMap<>() {
        {
            put('1', 0);
            put('2', 1);
            put('3', 2);
            put('4', 3);
            put('5', 4);
            put('6', 5);
            put('7', 6);
            put('8', 7);
            put('9', 8);
            put('0', 9);
            put('*', 10);
            put('#', 11);
        }
    };

    /**
     * 单例模式获取一个 {@code SchemaUtil} 实例。
     *
     * @return {@code SchemaUtil} 实例
     */
    public static IvrMap getInstance() {
        if (schema == null) {
            synchronized (IvrMap.class) {
                if (schema == null) {
                    schema = new IvrMap();
                }
            }
        }
        return schema;
    }

    /**
     * 获取有序的 trigger-event 映射
     *
     * @return trigger -event 映射表
     */
    public Map<String, List<String>> getMap() {
        return map;
    }

    /**
     * 添加一组 trigger-event 映射关系
     *
     * @param path      事件节点的绝对路径
     * @param event     事件
     * @param action    事件触发动作
     * @param additions 可能用到的补充信息
     */
    public void put(String path, String event, String action, String additions) {
        map.put(path, new ArrayList<>() {{
            add(event);
            add(action);
            add(additions);
        }});
    }

    /**
     * 删除一组 trigger-event 映射关系
     *
     * @param path 事件节点的绝对路径
     */
    public void remove(String path) {
        map.entrySet().removeIf(item -> item.getKey().startsWith(path));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, List<String>>> entrySet = map.entrySet();
        List<Map.Entry<String, List<String>>> list = new ArrayList<>(entrySet);
        list.sort(getInstance());
        for (Map.Entry<String, List<String>> item : list) {
            String path = item.getKey();
            String value = item.getValue().toString();
            sb.append("\t".repeat(path.length() - 1));
            sb.append(path).append(" --> ").append(value).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int compare(Map.Entry<String, List<String>> o1, Map.Entry<String, List<String>> o2) {
        String s1 = o1.getKey();
        String s2 = o2.getKey();
        int len1 = s1.length();
        int len2 = s2.length();
        for (int i = 0; i < len1 && i < len2; i++) {
            if (!ORDER_MAP.get(s1.charAt(i)).equals(ORDER_MAP.get(s2.charAt(i)))) {
                return ORDER_MAP.get(s1.charAt(i)) - ORDER_MAP.get(s2.charAt(i));
            } else {
                if (i == len1 - 1 || i == len2 - 1) {
                    return len1 - len2;
                }
            }
        }
        return 0;
    }
}
