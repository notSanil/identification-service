INSERT INTO public.contact (id, created_at,deleted_at,email,link_precedence,phone_number,updated_at,linked_id) VALUES
	 (2, '2024-05-24 12:12:18.369',NULL,'mcfly@hillvalley.edu','Secondary','123456','2024-05-24 12:12:18.369',1),
	 (1, '2024-05-24 12:12:18.369',NULL,'lorraine@hillvalley.edu','Primary','123456','2024-05-24 12:12:18.369',NULL) ON CONFLICT DO NOTHING;
