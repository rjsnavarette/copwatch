# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)

Admin.create_test_admin if Admin.count == 0 && Rails.env != 'production'
User.seed               if User.count == 0 && Rails.env != 'production'
Category.seed           if Category.count == 0
EmailTemplate.seed_test if EmailTemplate.count == 0 && Rails.env != 'production'
Feedback.seed           if Feedback.count == 0 && Rails.env != 'production'
Page.seed               if Page.count == 0
FreeAd.seed             if FreeAd.count == 0