--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3 (Debian 15.3-1.pgdg120+1)
-- Dumped by pg_dump version 15.3 (Debian 15.3-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: identification
--

INSERT INTO public.contact VALUES (1, '2024-05-24 17:28:04.929', NULL, 'lorraine@hillvalley.edu', 'Primary', '123456', '2024-05-24 17:28:04.929', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.contact VALUES (2, '2024-05-24 17:28:04.929', NULL, 'mcfly@hillvalley.edu', 'Secondary', '123456', '2024-05-24 17:28:04.929', 1) ON CONFLICT DO NOTHING;


--
-- Name: contact_id_seq; Type: SEQUENCE SET; Schema: public; Owner: identification
--

SELECT pg_catalog.setval('public.contact_id_seq', 2, true);


--
-- PostgreSQL database dump complete
--

