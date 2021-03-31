class Admin::FeedsController < ApplicationController
  def index
    @feeds  = Feed.includes(:user)
                    .select("feeds .*, users.id, users.first_name, users.last_name")
                    .references("users").all
  end

  def show
  end

  def destoy
  end
end
