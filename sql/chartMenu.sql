-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图', '3', '1', '/lb/chart', 'C', '0', 'lb:chart:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '轮播图菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图查询', @parentId, '1',  '#',  'F', '0', 'lb:chart:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图新增', @parentId, '2',  '#',  'F', '0', 'lb:chart:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图修改', @parentId, '3',  '#',  'F', '0', 'lb:chart:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('轮播图删除', @parentId, '4',  '#',  'F', '0', 'lb:chart:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');
