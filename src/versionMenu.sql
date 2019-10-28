-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('app版本号管理', '3', '1', '/lb/version', 'C', '0', 'lb:version:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', 'app版本号管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('app版本号管理查询', @parentId, '1',  '#',  'F', '0', 'lb:version:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('app版本号管理新增', @parentId, '2',  '#',  'F', '0', 'lb:version:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('app版本号管理修改', @parentId, '3',  '#',  'F', '0', 'lb:version:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('app版本号管理删除', @parentId, '4',  '#',  'F', '0', 'lb:version:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');
