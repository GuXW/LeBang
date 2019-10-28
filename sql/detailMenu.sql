-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户转账明细', '3', '1', '/lb/detail', 'C', '0', 'lb:detail:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '用户转账明细菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户转账明细查询', @parentId, '1',  '#',  'F', '0', 'lb:detail:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户转账明细新增', @parentId, '2',  '#',  'F', '0', 'lb:detail:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户转账明细修改', @parentId, '3',  '#',  'F', '0', 'lb:detail:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户转账明细删除', @parentId, '4',  '#',  'F', '0', 'lb:detail:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');
