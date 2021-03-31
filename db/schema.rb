# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# This file is the source Rails uses to define your schema when running `rails
# db:schema:load`. When creating a new database, `rails db:schema:load` tends to
# be faster and is potentially less error prone than running all of your
# migrations from scratch. Old migrations may fail to apply correctly if those
# migrations use external dependencies or application code.
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 2021_03_31_064410) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "plpgsql"

  create_table "admins", force: :cascade do |t|
    t.string "email", default: "", null: false
    t.string "encrypted_password", default: "", null: false
    t.string "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.string "first_name", default: ""
    t.string "last_name", default: ""
    t.string "photo"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["email"], name: "index_admins_on_email", unique: true
    t.index ["first_name"], name: "index_admins_on_first_name"
    t.index ["last_name"], name: "index_admins_on_last_name"
    t.index ["reset_password_token"], name: "index_admins_on_reset_password_token", unique: true
  end

  create_table "categories", force: :cascade do |t|
    t.string "name"
    t.integer "category_type", default: 0
    t.integer "sub_type", default: 0
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["category_type"], name: "index_categories_on_category_type"
    t.index ["name"], name: "index_categories_on_name"
    t.index ["sub_type"], name: "index_categories_on_sub_type"
  end

  create_table "email_templates", force: :cascade do |t|
    t.string "subject"
    t.string "greetings"
    t.string "content"
    t.string "closing"
    t.integer "category_id"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["category_id"], name: "index_email_templates_on_category_id"
  end

  create_table "feedbacks", force: :cascade do |t|
    t.integer "user_id"
    t.string "description"
    t.string "image"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["user_id"], name: "index_feedbacks_on_user_id"
  end

  create_table "feeds", force: :cascade do |t|
    t.text "feed"
    t.text "comment"
    t.text "reply"
    t.string "like"
    t.string "image"
    t.integer "user_id"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["user_id"], name: "index_feeds_on_user_id"
  end

  create_table "free_ads", force: :cascade do |t|
    t.string "name"
    t.float "price", default: 0.0
    t.integer "currency", default: 0
    t.integer "duration", default: 0
    t.integer "duration_type", default: 2
    t.boolean "is_active", default: true
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["duration"], name: "index_free_ads_on_duration"
    t.index ["duration_type"], name: "index_free_ads_on_duration_type"
    t.index ["is_active"], name: "index_free_ads_on_is_active"
    t.index ["name"], name: "index_free_ads_on_name"
    t.index ["price"], name: "index_free_ads_on_price"
  end

  create_table "notifications", force: :cascade do |t|
    t.integer "user_id"
    t.integer "notifiable_id"
    t.string "notifiable_type"
    t.string "title"
    t.string "description"
    t.boolean "is_read", default: false
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["is_read"], name: "index_notifications_on_is_read"
    t.index ["notifiable_id"], name: "index_notifications_on_notifiable_id"
    t.index ["notifiable_type"], name: "index_notifications_on_notifiable_type"
    t.index ["user_id"], name: "index_notifications_on_user_id"
  end

  create_table "pages", force: :cascade do |t|
    t.string "title"
    t.string "content"
    t.integer "page_type"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["page_type"], name: "index_pages_on_page_type"
    t.index ["title"], name: "index_pages_on_title"
  end

  create_table "permissions", force: :cascade do |t|
    t.integer "user_id"
    t.boolean "has_camera", default: false
    t.boolean "has_cloud_communication", default: false
    t.boolean "has_phone_lock_dnd_screen_dim", default: false
    t.boolean "has_siri_bixby_google_interface_command", default: false
    t.boolean "has_advance_agreed", default: false
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["has_camera"], name: "index_permissions_on_has_camera"
    t.index ["has_cloud_communication"], name: "index_permissions_on_has_cloud_communication"
    t.index ["has_phone_lock_dnd_screen_dim"], name: "index_permissions_on_has_phone_lock_dnd_screen_dim"
    t.index ["has_siri_bixby_google_interface_command"], name: "index_permissions_on_has_siri_bixby_google_interface_command"
  end

  create_table "post_feeds", force: :cascade do |t|
    t.text "feed"
    t.text "comment"
    t.text "reply"
    t.string "like"
    t.string "image"
    t.integer "user_id"
    t.index ["user_id"], name: "index_post_feeds_on_user_id"
  end

  create_table "preferences", force: :cascade do |t|
    t.integer "user_id"
    t.boolean "is_recording_to_cloud", default: true
    t.boolean "is_dim_my_screen", default: true
    t.boolean "is_do_not_disturb", default: true
    t.boolean "is_recording_audio_video", default: true
    t.boolean "is_voice_activated", default: true
    t.boolean "is_rear_camera_selected", default: true
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["user_id"], name: "index_preferences_on_user_id"
  end

  create_table "subscriptions", force: :cascade do |t|
    t.integer "user_id"
    t.integer "itemable_id"
    t.string "itemable_type"
    t.datetime "expiry_at"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["expiry_at"], name: "index_subscriptions_on_expiry_at"
    t.index ["itemable_id"], name: "index_subscriptions_on_itemable_id"
    t.index ["itemable_type"], name: "index_subscriptions_on_itemable_type"
    t.index ["user_id"], name: "index_subscriptions_on_user_id"
  end

  create_table "users", force: :cascade do |t|
    t.string "email"
    t.string "first_name"
    t.string "last_name"
    t.string "phone_number"
    t.string "photo"
    t.string "password"
    t.string "auth_token"
    t.string "device_token"
    t.string "verification_token"
    t.string "password_reset_token"
    t.string "uuid"
    t.integer "account_type", default: 0
    t.integer "default_storage_type", default: 0
    t.integer "mode_type", default: 0
    t.boolean "is_verified", default: false
    t.boolean "is_notification_on", default: true
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.string "google_account_email"
    t.index ["account_type"], name: "index_users_on_account_type"
    t.index ["auth_token"], name: "index_users_on_auth_token"
    t.index ["default_storage_type"], name: "index_users_on_default_storage_type"
    t.index ["email"], name: "index_users_on_email"
    t.index ["first_name"], name: "index_users_on_first_name"
    t.index ["google_account_email"], name: "index_users_on_google_account_email"
    t.index ["is_verified"], name: "index_users_on_is_verified"
    t.index ["last_name"], name: "index_users_on_last_name"
    t.index ["mode_type"], name: "index_users_on_mode_type"
    t.index ["password_reset_token"], name: "index_users_on_password_reset_token"
    t.index ["phone_number"], name: "index_users_on_phone_number"
    t.index ["uuid"], name: "index_users_on_uuid"
    t.index ["verification_token"], name: "index_users_on_verification_token"
  end

  create_table "videos", force: :cascade do |t|
    t.integer "user_id"
    t.string "title"
    t.string "link"
    t.integer "parent_video_id"
    t.integer "sequence_index"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.index ["parent_video_id"], name: "index_videos_on_parent_video_id"
    t.index ["sequence_index"], name: "index_videos_on_sequence_index"
    t.index ["title"], name: "index_videos_on_title"
    t.index ["user_id"], name: "index_videos_on_user_id"
  end

end
