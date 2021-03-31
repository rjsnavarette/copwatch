# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)

puts "\n-- Seeding Users --\n"
User.seed               if User.count == 0 && Rails.env != 'production'
puts "\n-- Seeding Users Feedbacks --\n"
Feedback.seed           if Feedback.count == 0 && Rails.env != 'production'
puts "\n-- Seeding Free Ads --\n"
FreeAd.seed             if FreeAd.count == 0
puts "\n-- Seeding Admin --\n"
Admin.seed              if Admin.count == 0
puts "\n-- Seeding Feeds --\n"
Feed.seed
