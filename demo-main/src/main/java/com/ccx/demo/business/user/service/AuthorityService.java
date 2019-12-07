package com.ccx.demo.business.user.service;

import com.ccx.demo.business.user.entity.Authority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ccx.demo.business.user.enums.AuthorityCode.*;

/**
 * <pre>
 * 权限服务
 *
 * @author 谢长春 2019/8/27
 */
@Slf4j
@Service
public class AuthorityService {
    // 构造权限树
    private static final List<Authority> TREE = Arrays.asList(
            Menu_Home.build(),
            Menu_Setting.nodes(
                    Menu_User.nodes(
                            UserController_page.build(),
                            UserController_save.build(),
                            UserController_update.build(),
                            UserController_reset.build()
                    ),
                    Menu_Role.nodes(
                            RoleController_page.build(),
                            RoleController_save.build(),
                            RoleController_update.build()
                    )
            )
    );

    // 展开树节点
    private static final List<Authority> LIST = recursion(TREE);

    /**
     * 递归展开树节点
     *
     * @param tree {@link List<Authority>}
     * @return {@link List<Authority>}
     */
    private static List<Authority> recursion(final List<Authority> tree) {
        final List<Authority> list = new ArrayList<>();
        tree.forEach(node -> {
            if (Objects.nonNull(node.getNodes()) && !node.getNodes().isEmpty()) {
                list.addAll(recursion(node.getNodes()).stream().peek(item -> item.setParentCode(node.getCode())).collect(Collectors.toList()));
            }
            list.add(node);
        });
        return list.stream().map(Authority::clone).peek(obj -> obj.setNodes(null)).collect(Collectors.toList());
    }

    /**
     * 递归展开树节点，checked = true 且子节点数量为空的父节点也排除
     *
     * @param tree {@link List<Authority>}
     * @return {@link List<Authority>}
     */
    private static List<Authority> recursionChecked(final List<Authority> tree) {
        final List<Authority> list = new ArrayList<>();
        tree.forEach(node -> {
            if (Objects.nonNull(node.getNodes()) && !node.getNodes().isEmpty()) {
                final List<Authority> nodes = recursionChecked(node.getNodes()).stream()
                        .filter(Authority::isChecked)
                        .peek(item -> item.setParentCode(node.getCode()))
                        .collect(Collectors.toList());
                if (!nodes.isEmpty()) {
                    node.setChecked(true);
                    list.add(node);
                    list.addAll(nodes);
                }
            } else if (node.isChecked()) {
                list.add(node);
            }
        });
        return list.stream().map(Authority::clone).peek(obj -> obj.setNodes(null)).collect(Collectors.toList());
    }

    /**
     * 获取全部权限配置树集合
     *
     * @return {@link List<Authority>}
     */
    public List<Authority> getTree() {
        return TREE;
    }

    /**
     * 获取全部权限配置集合，树子节点展开
     *
     * @return {@link List<Authority>}
     */
    public List<Authority> getList() {
        return LIST;
    }

    /**
     * 展开指定的权限树
     *
     * @param tree {@link List<Authority>}
     * @return {@link List<Authority>}
     */
    public List<Authority> expendList(final List<Authority> tree) {
        return recursion(tree);
    }

    /**
     * 展开指定的权限树，且 checked 必须是选中状态
     *
     * @param tree {@link List<Authority>}
     * @return {@link List<Authority>}
     */
    public List<Authority> expendFilterCheckedList(final List<Authority> tree) {
        return recursionChecked(tree);
    }

//    public static void main(String[] args) {
//        System.out.println(JSON.toJSONString(TREE));
//        System.out.println(JSON.toJSONString(LIST));
//        System.out.println(new PermissionService().expendFilterCheckedList(
//                JSON.parseArray("[{\"code\": \"Menu_Home\", \"name\": \"主页\", \"type\": \"MENU\", \"nodes\": [{\"code\": \"HomeController_getDemoA\", \"name\": \"虚拟查询接口1\", \"type\": \"LOAD\", \"checked\": true}, {\"code\": \"HomeController_getDemoB\", \"name\": \"虚拟查询接口2\", \"type\": \"LOAD\", \"checked\": false}, {\"code\": \"HomeController_getDemoC\", \"name\": \"虚拟查询接口3\", \"type\": \"LOAD\", \"checked\": false}], \"checked\": false}, {\"code\": \"Menu_User_Setting\", \"name\": \"用户权限\", \"type\": \"MENU\", \"nodes\": [{\"code\": \"Menu_User\", \"name\": \"用户\", \"type\": \"MENU\", \"nodes\": [{\"code\": \"UserController_page\", \"name\": \"分页查询\", \"type\": \"LOAD\", \"checked\": false}, {\"code\": \"UserController_save\", \"name\": \"新增按钮，保存数据\", \"type\": \"BUTTON\", \"checked\": false}], \"checked\": false}, {\"code\": \"Menu_Role\", \"name\": \"角色\", \"type\": \"MENU\", \"nodes\": [{\"code\": \"RoleController_page\", \"name\": \"分页查询\", \"type\": \"LOAD\", \"checked\": true}, {\"code\": \"RoleController_save\", \"name\": \"新增按钮，保存数据\", \"type\": \"BUTTON\", \"checked\": false}, {\"code\": \"RoleController_update\", \"name\": \"更新按钮，更新单条数据\", \"type\": \"BUTTON\", \"checked\": false}, {\"code\": \"RoleController_markDeleteById\", \"name\": \"逻辑删除按钮，逻辑删除单条数据\", \"type\": \"BUTTON\", \"checked\": false}, {\"code\": \"RoleController_markDeleteByIds\", \"name\": \"批量逻辑删除按钮，批量逻辑删除多条数据\", \"type\": \"BUTTON\", \"checked\": false}, {\"code\": \"RoleController_getById\", \"name\": \"查看详情按钮，查看单条数据详情\", \"type\": \"BUTTON\", \"checked\": false}], \"checked\": false}, {\"code\": \"Menu_Permission\", \"name\": \"权限\", \"type\": \"MENU\", \"nodes\": [{\"code\": \"PermissionController_list\", \"name\": \"查询列表\", \"type\": \"LOAD\", \"checked\": true}], \"checked\": false}], \"checked\": false}, {\"code\": \"Menu_Setting\", \"name\": \"系统设置\", \"type\": \"MENU\", \"nodes\": [{\"code\": \"Menu_Setting_Config\", \"name\": \"系统配置信息\", \"type\": \"MENU\", \"nodes\": [{\"code\": \"AdminConfigController_getApp\", \"name\": \"查看 APP 配置参数\", \"type\": \"LOAD\", \"checked\": false}, {\"code\": \"AdminConfigController_getPath\", \"name\": \"查看 APP 文件路径\", \"type\": \"LOAD\", \"checked\": false}, {\"code\": \"AdminConfigController_getUrl\", \"name\": \"查看 APP 文件访问地址\", \"type\": \"LOAD\", \"checked\": false}, {\"code\": \"AdminConfigController_getByKey\", \"name\": \"查看 APP 指定配置\", \"type\": \"LOAD\", \"checked\": false}], \"checked\": false}], \"checked\": true}]", AuthorityCode.class)
//        ));
//    }
}