-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台配置', '3', '1', '/lb/property', 'C', '0', 'lb:property:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '平台配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台配置查询', @parentId, '1',  '#',  'F', '0', 'lb:property:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台配置新增', @parentId, '2',  '#',  'F', '0', 'lb:property:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台配置修改', @parentId, '3',  '#',  'F', '0', 'lb:property:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台配置删除', @parentId, '4',  '#',  'F', '0', 'lb:property:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');
