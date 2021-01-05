class CreateFeedbacks < ActiveRecord::Migration[6.0]
  def change
    create_table :feedbacks do |t|
      t.integer :user_id
      t.string :description
      t.string :image

      t.timestamps
    end
    add_index :feedbacks, :user_id
  end
end
