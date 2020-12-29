class CreateUsers < ActiveRecord::Migration[6.0]
  def change
    create_table :users do |t|
      t.string :email
      t.string :first_name
      t.string :last_name
      t.string :phone_number
      t.string :photo
      t.string :password
      t.string :auth_token
      t.string :verification_token
      t.string :password_reset_token
      t.string :uuid
      t.integer :account_type, default: 0
      t.integer :default_storage_type, default: 0
      t.integer :mode_type, default: 0
      t.boolean :is_verified, default: false

      t.timestamps
    end
    add_index :users, :email
    add_index :users, :first_name
    add_index :users, :last_name
    add_index :users, :phone_number
    add_index :users, :auth_token
    add_index :users, :verification_token
    add_index :users, :password_reset_token
    add_index :users, :uuid
  end
end
