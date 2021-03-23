class AddGoogleAccountEmailToUsers < ActiveRecord::Migration[6.0]
  def change
    add_column :users, :google_account_email, :string
    add_index :users, :google_account_email
  end
end
