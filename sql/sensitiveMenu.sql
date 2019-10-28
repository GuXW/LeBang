-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户敏感数据', '3', '1', '/lb/sensitive', 'C', '0', 'lb:sensitive:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '用户敏感数据菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户敏感数据查询', @parentId, '1',  '#',  'F', '0', 'lb:sensitive:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户敏感数据新增', @parentId, '2',  '#',  'F', '0', 'lb:sensitive:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户敏感数据修改', @parentId, '3',  '#',  'F', '0', 'lb:sensitive:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户敏感数据删除', @parentId, '4',  '#',  'F', '0', 'lb:sensitive:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');
