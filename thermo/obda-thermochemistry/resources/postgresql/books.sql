PGDMP         1                x            books    12.3    12.3                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16398    books    DATABASE     �   CREATE DATABASE books WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';
    DROP DATABASE books;
                postgres    false            �            1259    16399    tb_books    TABLE     a   CREATE TABLE public.tb_books (
    bk_code text NOT NULL,
    bk_title text,
    bk_type text
);
    DROP TABLE public.tb_books;
       public         heap    postgres    false            �
          0    16399    tb_books 
   TABLE DATA           >   COPY public.tb_books (bk_code, bk_title, bk_type) FROM stdin;
    public          postgres    false    202   S       
           2606    16406    tb_books tb_books_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tb_books
    ADD CONSTRAINT tb_books_pkey PRIMARY KEY (bk_code);
 @   ALTER TABLE ONLY public.tb_books DROP CONSTRAINT tb_books_pkey;
       public            postgres    false    202            �
   1   x�3�L��ώ/�,�I�7�t�2B0�t�2F0�0A0������ U��     